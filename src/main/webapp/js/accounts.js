"use strict";

document.addEventListener("DOMContentLoaded", function () {

    var listElem = document.getElementById("list").firstElementChild;

    var formElem = document.getElementById("form");
    var idElem = document.getElementById("id");
    var currencyIdElem = document.getElementById("currencyId");
    var sortElem = document.getElementById("sort");
    var titleElem = document.getElementById("title");
    var saveBtnElem = document.getElementById("save");
    var newBtnElem = document.getElementById("new");

    var entityList = [];
    var currentEntity = null;

    var resourceUrl = appCtx + "/rest/accounts";
    var currencyResourceUrl = appCtx + "/rest/currencies";

    jQuery.getJSON(currencyResourceUrl, function (data) {
        data.forEach(function (entity) {
            var optionElem = document.createElement("option");
            optionElem.value = entity.id;
            optionElem.text = "[" + entity.code + "] " + entity.title;
            currencyIdElem.add(optionElem);
        });
        currencyIdElem.value = null;
    });

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
            currencyId: currencyIdElem.value ? +currencyIdElem.value : null,
            sort: sortElem.value,
            title: titleElem.value
        };
    }

    function clearForm() {
        currentEntity = null;
        idElem.value = "";
        currencyIdElem.value = null;
        sortElem.value = "";
        titleElem.value = "";
    }

    function fillForm(entity) {
        idElem.value = entity.id;
        currencyIdElem.value = entity.currencyId;
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
        cellElem.innerText = entity.currencyCode;
        cellElem = rowElem.appendChild(document.createElement("td"));
        cellElem.innerText = entity.currencyTitle;
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
        rowElem.children[1].innerText = entity.currencyCode;
        rowElem.children[2].innerText = entity.currencyTitle;
        rowElem.children[3].innerText = entity.sort;
        rowElem.children[4].innerText = entity.title;
    }

    function copyEntity(source, target) {
        target.id = source.id;
        target.currencyCode = source.currencyCode;
        target.currencyTitle = source.currencyTitle;
        target.sort = source.sort;
        target.title = source.title;
    }

});
