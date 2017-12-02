"use strict";

var formComponents = {
    edId: document.getElementById("edId"),
    cbAccount: document.getElementById("cbAccount"),
    cbCategory: document.getElementById("cbCategory"),
    edDay: document.getElementById("edDay"),
    edAmount: document.getElementById("edAmount"),
    edNote: document.getElementById("edNote"),
    btNew: document.getElementById("btNew"),
    btSave: document.getElementById("btSave"),

    cbCurrency: document.getElementById("cbCurrencyId"),
    lsAccounts: document.getElementById("lsAccounts"),
    edFirstDay: document.getElementById("edFirstDay"),
    edLastDay: document.getElementById("edLastDay"),
    btFilter: document.getElementById("btFilter"),

    lsOperations: document.getElementById("lsOperations")
};

formComponents.btNew.addEventListener("click", doOnBtNewCLick);
formComponents.btSave.addEventListener("click", doOnBtSaveClick);
formComponents.cbCurrency.addEventListener("change", doOnCbCurrencyChange);
formComponents.btFilter.addEventListener("click", doOnBtFilterClick);

var resourceUrls = {
    currencies: appCtx + "/rest/currency",
    accounts: appCtx + "/rest/account",
    operations: appCtx + "/rest/operations",
    categories: appCtx + "/rest/category"
};

var entityList = [];
var currentEntity = null;

jQuery.getJSON(resourceUrls.currencies, function (data) {
    data.forEach(function (entity) {
        var optionElem = document.createElement("option");
        optionElem.value = entity.id;
        optionElem.text = "[" + entity.code + "] " + entity.title;
        formComponents.cbCurrency.add(optionElem);
    });
    formComponents.cbCurrency.value = null;
});

jQuery.getJSON(resourceUrls.categories, function (data) {
    data.forEach(function (entity) {
        var optionElem = document.createElement("option");
        optionElem.value = entity.id;
        optionElem.text = entity.title;
        formComponents.cbCategory.add(optionElem);
    });
    formComponents.cbCategory.value = null;
});


function doOnCbCurrencyChange() {
    clearLsAccounts();
    clearCbAccount();

    var currencyId = formComponents.cbCurrency.value;
    if (!currencyId) {
        return;
    }
    jQuery.getJSON(resourceUrls.accounts + "?currencyId=" + currencyId, function (data) {
        data.forEach(function (entity) {
            var optionElem = document.createElement("option");
            optionElem.value = entity.id;
            optionElem.text = entity.title;
            formComponents.lsAccounts.add(optionElem);
            optionElem = document.createElement("option");
            optionElem.value = entity.id;
            optionElem.text = entity.title;
            formComponents.cbAccount.add(optionElem);
        });
    });
}

function doOnBtFilterClick() {
    var accountIds = [];
    var accountOption = formComponents.lsAccounts.firstElementChild;
    while (accountOption) {
        if (accountOption.selected) {
            accountIds.push(accountOption.value);
        }
        accountOption = accountOption.nextElementSibling;
    }
    if (!accountIds.length) {
        return;
    }
    var firstDay = formComponents.edFirstDay.value;
    var lastDay = formComponents.edLastDay.value;
    var url = resourceUrls.operations + "?first_day=" + firstDay + "&last_day=" + lastDay;
    accountIds.forEach(function (accountId) {
        url += "&account_id=" + accountId;
    });
    jQuery.getJSON(url, function (data) {
        clearLsOperations();
        entityList = data;
        data.forEach(function (entity) {
            appendRow(entity);
        });
    });
}

function doOnBtNewCLick() {
    clearForm();
}

function doOnBtSaveClick() {
    var linkedRow = currentEntity ? currentEntity.linkedRow : null;
    var entity = getEntityFromForm();
    clearForm();

    jQuery.ajax({
        url: resourceUrls.operations,
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

function doOnBtEditClick() {
    fillForm(this.parentElement.parentElement.linkedEntity);
}

function doOnBtDeleteClick() {
    var linkedRow = this.parentElement.parentElement;
    var entity = linkedRow.linkedEntity;

    jQuery.ajax({
        url: resourceUrls.operations + "/" + entity.id,
        method: "delete",
        success: doOnDeleteSuccess,
        error: doOnError
    });

    function doOnDeleteSuccess() {
        for (var index = 0; index < entityList.length; index++) {
            if (entityList[index] === entity) {
                formComponents.lsOperations.removeChild(entity.linkedRow);
                entityList.splice(index, 1);
            }
        }
    }

    function doOnError() {
        console.log("error");
    }
}

function getEntityFromForm() {
    var result = {
        accountId: formComponents.cbAccount.value,
        categoryId: formComponents.cbCategory.value,
        day: formComponents.edDay.value,
        amount: formComponents.edAmount.value,
        note: formComponents.edNote.value
    };
    if (formComponents.edId.value) {
        result.id = formComponents.edId.value;
    }
    return result;
}

function clearForm() {
    currentEntity = null;
    formComponents.edId.value = "";
    formComponents.cbAccount.value = null;
    formComponents.cbAccount.disabled = false;
    formComponents.cbCategory.value = null;
    formComponents.cbCategory.disabled = false;
    formComponents.edDay.value = "";
    formComponents.edAmount.value = "";
    formComponents.edNote.value = "";
}

function fillForm(entity) {
    currentEntity = entity;
    formComponents.edId.value = entity.id;
    formComponents.cbAccount.value = entity.accountId;
    formComponents.cbAccount.disabled = true;
    formComponents.cbCategory.value = entity.categoryId;
    formComponents.cbCategory.disabled = true;
    formComponents.edDay.value = entity.day;
    formComponents.edAmount.value = entity.amount;
    formComponents.edNote.value = entity.note;
}

function appendRow(entity) {
    var rowElem = formComponents.lsOperations.appendChild(document.createElement("tr"));
    rowElem.linkedEntity = entity;
    entity.linkedRow = rowElem;
    var cellElem = rowElem.appendChild(document.createElement("td"));
    cellElem.innerText = entity.id;
    cellElem = rowElem.appendChild(document.createElement("td"));
    cellElem.innerText = entity.accountTitle;
    cellElem = rowElem.appendChild(document.createElement("td"));
    cellElem.innerText = entity.categoryTitle;
    cellElem = rowElem.appendChild(document.createElement("td"));
    cellElem.innerText = entity.day;
    cellElem = rowElem.appendChild(document.createElement("td"));
    cellElem.innerText = entity.amount;
    cellElem = rowElem.appendChild(document.createElement("td"));
    cellElem.innerText = entity.note;
    cellElem = rowElem.appendChild(document.createElement("td"));
    var btnElem = cellElem.appendChild(document.createElement("button"));
    btnElem.innerText = "Edit";
    btnElem.onclick = doOnBtEditClick;
    btnElem = cellElem.appendChild(document.createElement("button"));
    btnElem.innerText = "Delete";
    btnElem.onclick = doOnBtDeleteClick;
}

function updateRow(rowElem, entity) {
    rowElem.children[0].innerText = entity.id;
    rowElem.children[1].innerText = entity.accountTitle;
    rowElem.children[2].innerText = entity.categoryTitle;
    rowElem.children[3].innerText = entity.day;
    rowElem.children[4].innerText = entity.amount;
    rowElem.children[5].innerText = entity.note;
}

function copyEntity(source, target) {
    target.id = source.id;
    target.accountId = source.accountId;
    target.accountTitle = source.accountTitle;
    target.categoryId = source.categoryId;
    target.categoryTitle = source.categoryTitle;
    target.day = source.day;
    target.amount = source.amount;
    target.note = source.note;
}

function clearLsOperations() {
    var elem = formComponents.lsOperations;
    while (elem.children.length > 1) {
        elem.removeChild(elem.lastElementChild);
    }
}

function clearLsAccounts() {
    var elem = formComponents.lsAccounts;
    while (elem.children.length) {
        elem.removeChild(elem.lastElementChild);
    }
}

function clearCbAccount() {
    var elem = formComponents.cbAccount;
    while (elem.children.length) {
        elem.removeChild(elem.lastElementChild);
    }
}
