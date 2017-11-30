"use strict";

"use strict";

document.addEventListener("DOMContentLoaded", function () {

    var listElem = document.getElementById("list").firstElementChild;

    var formElem = document.getElementById("form");
    var idElem = document.getElementById("id");
    var codeElem = document.getElementById("code");
    var titleElem = document.getElementById("title");
    var accuracyElem = document.getElementById("accuracy");
    var saveBtnElem = document.getElementById("save");
    var newBtnElem = document.getElementById("new");

    var entityList = [];
    var currentEntity = null;

    var resourceUrl = appCtx + "/rest/currency";

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
            code: codeElem.value,
            title: titleElem.value,
            accuracy: accuracyElem.value
        };
    }

    function clearForm() {
        currentEntity = null;
        idElem.value = "";
        codeElem.value = "";
        titleElem.value = "";
        accuracyElem.value = "";
    }

    function fillForm(entity) {
        idElem.value = entity.id;
        codeElem.value = entity.code;
        titleElem.value = entity.title;
        accuracyElem.value = entity.accuracy;
    }

    function appendRow(entity) {
        var rowElem = listElem.appendChild(document.createElement("tr"));
        rowElem.linkedEntity = entity;
        entity.linkedRow = rowElem;
        var cellElem = rowElem.appendChild(document.createElement("td"));
        cellElem.innerText = entity.id;
        cellElem = rowElem.appendChild(document.createElement("td"));
        cellElem.innerText = entity.code;
        cellElem = rowElem.appendChild(document.createElement("td"));
        cellElem.innerText = entity.title;
        cellElem = rowElem.appendChild(document.createElement("td"));
        cellElem.innerText = entity.accuracy;
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
        rowElem.children[1].innerText = entity.code;
        rowElem.children[2].innerText = entity.title;
        rowElem.children[3].innerText = entity.accuracy;
    }

    function copyEntity(source, target) {
        target.id = source.id;
        target.code = source.code;
        target.title = source.title;
        target.accuracy = source.accuracy;
    }

});
