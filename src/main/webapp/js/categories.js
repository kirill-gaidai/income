"use strict";

document.addEventListener("DOMContentLoaded", function () {

    var listElem = document.getElementById("list").firstElementChild;

    var formElem = document.getElementById("form");
    var idElem = document.getElementById("id");
    var sortElem = document.getElementById("sort");
    var titleElem = document.getElementById("title");
    var saveBtnElem = document.getElementById("save");
    var newBtnElem = document.getElementById("new");

    var entityList = [];
    var currentEntity = null;

    var resourceUrl = appCtx + "/rest/categories";

    jQuery.getJSON(resourceUrl, function (data) {
        entityList = data;
        entityList.forEach(function (entity) {
            appendRow(entity);
        });
    });

    formElem.addEventListener("submit", doOnFormSubmit);
    saveBtnElem.addEventListener("click", doOnSaveBtnClick);
    newBtnElem.addEventListener("click", doOnNewBtnClick);

    function doOnFormSubmit(event) {
        event.preventDefault();
    }

    function doOnNewBtnClick() {
        clearForm();
    }

    function doOnSaveBtnClick() {
        var linkedRow = currentEntity ? currentEntity.linkedRow : null;
        var entity = getEntityFromForm();
        clearForm();

        jQuery.ajax({
            url: resourceUrl,
            method: linkedRow ? "put" : "post",
            data: JSON.stringify(entity),
            contentType: "application/json",
            dataType: "json",
            success: linkedRow ? doOnPutSuccess : doOnPostSuccess,
            error: doOnError
        });

        function doOnPutSuccess(data) {
            updateRow(linkedRow, data);
            copyEntity(data, linkedRow.linkedEntity);
        }

        function doOnPostSuccess(data) {
            appendRow(data);
            entityList.push(data)
        }

        function doOnError() {
            console.log("error");
        }
    }

    function doOnEditBtnClick() {
        currentEntity = this.parentElement.parentElement.linkedEntity;
        fillForm(currentEntity);
    }

    function doOnDeleteBtnClick() {
        var linkedRow = this.parentElement.parentElement;
        var entity = linkedRow.linkedEntity;

        jQuery.ajax({
            url: resourceUrl + "/" + entity.id,
            method: "delete",
            success: doOnDeleteSuccess,
            error: doOnError
        });

        function doOnDeleteSuccess() {
            for (var index = 0; index < entityList.length; index++) {
                if (entityList[index] === entity) {
                    listElem.removeChild(entity.linkedRow);
                    entityList.splice(index, 1);
                }
            }
        }

        function doOnError() {
            console.log("error");
        }
    }

    function getEntityFromForm() {
        return {
            id: idElem.value ? idElem.value : null,
            sort: sortElem.value,
            title: titleElem.value
        };
    }

    function clearForm() {
        currentEntity = null;
        idElem.value = "";
        sortElem.value = "";
        titleElem.value = "";
    }

    function fillForm(entity) {
        idElem.value = entity.id;
        sortElem.value = entity.sort;
        titleElem.value = entity.title;
    }

    function appendRow(entity) {
        var rowElem = listElem.appendChild(document.createElement("tr"));
        rowElem.linkedEntity = entity;
        entity.linkedRow = rowElem;
        var cellElem = rowElem.appendChild(document.createElement("td"));
        cellElem.innerText = entity.id;
        cellElem = rowElem.appendChild(document.createElement("td"));
        cellElem.innerText = entity.sort;
        cellElem = rowElem.appendChild(document.createElement("td"));
        cellElem.innerText = entity.title;
        cellElem = rowElem.appendChild(document.createElement("td"));
        var btnElem = cellElem.appendChild(document.createElement("button"));
        btnElem.innerText = "Edit";
        btnElem.onclick = doOnEditBtnClick;
        btnElem = cellElem.appendChild(document.createElement("button"));
        btnElem.innerText = "Delete";
        btnElem.onclick = doOnDeleteBtnClick;
    }

    function updateRow(rowElem, entity) {
        rowElem.children[0].innerText = entity.id;
        rowElem.children[1].innerText = entity.sort;
        rowElem.children[2].innerText = entity.title;
    }

    function copyEntity(source, target) {
        target.id = source.id;
        target.sort = source.sort;
        target.title = source.title;
    }

});
