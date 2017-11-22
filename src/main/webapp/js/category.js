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

    getList();

    formElem.addEventListener("submit", doOnFormSubmit);
    saveBtnElem.addEventListener("click", doOnSaveBtnClick);
    newBtnElem.addEventListener("click", doOnNewBtnClick);

    function getList() {
        jQuery.getJSON(appCtx + "/rest/category", function (data) {
            entityList = data;
            entityList.forEach(function (entity) {
                appendRow(entity);
            });
        });
    }

    function doOnFormSubmit(event) {
        event.preventDefault();
    }

    function doOnSaveBtnClick() {
        var linkedRow = currentEntity ? currentEntity.linkedRow : null;
        var entity = getEntityFromForm();
        currentEntity = null;
        clearForm();

        $.ajax({
            url: appCtx + "/rest/category",
            method: linkedRow ? "put" : "post",
            data: JSON.stringify(entity),
            contentType: "application/json",
            dataType: "json",
            success: linkedRow ? doOnPutSuccess : doOnPostSuccess,
            error: doOnError()
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

    function doOnNewBtnClick() {
        currentEntity = null;
        clearForm();
    }

    function doOnEditBtnClick() {
        currentEntity = this.parentElement.parentElement.linkedEntity;
        fillForm(currentEntity);
    }

    function doOnDeleteBtnClick(event) {

    }

    function getEntityFromForm() {
        return {
            id: idElem.value ? idElem.value : null,
            sort: sortElem.value,
            title: titleElem.value
        };
    }

    function clearForm() {
        idElem.value = "";
        sortElem.value = "";
        titleElem.value = "";
    }

    function fillForm(entity) {
        idElem.value = entity.id;
        sortElem.value = entity.sort;
        titleElem.value = entity.title;
    }

    function updateRow(rowElem, entity) {
        rowElem.children[0].innerText = entity.id;
        rowElem.children[1].innerText = entity.sort;
        rowElem.children[2].innerText = entity.title;
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

    function copyEntity(source, target) {
        target.id = source.id;
        target.sort = source.sort;
        target.title = source.title;
    }

});