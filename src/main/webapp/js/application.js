"use strict";

function Application() {
    this.resourceUrls = {
        accounts: appCtx + "/rest/accounts",
        categories: appCtx + "/rest/categories",
        currencies: appCtx + "/rest/currencies",
        balances: appCtx + "/rest/balances",
        operations: appCtx + "/rest/operations",
        summaries: appCtx + "/rest/summaries"
    };
}

let application = new Application();

Application.prototype.populateSelectOptions = function(selectElem, entityList, keyFieldName, valueFieldName) {
    while (selectElem.firstChild) {
        selectElem.remove(selectElem.firstChild);
    }
    entityList.forEach(entity => {
        let optionElem = document.createElement("option");
        optionElem.value = entity[keyFieldName];
        optionElem.text = entity[valueFieldName];
        selectElem.add(optionElem);
    });
    selectElem.value = null;
};

Application.prototype.isoStrToDate = function(str) {
    let tokens = str.split("-", 3);
    return new Date(+tokens[0], +tokens[1] - 1, +tokens[2]);
};

Application.prototype.dateToIsoStr = function(date) {
    let year = date.getFullYear() + "";
    let month = date.getMonth() + 1 + "";
    let day = date.getDate() + "";

    if (month.length < 2) {
        month = "0" + month;
    }
    if (day.length < 2) {
        day = "0" + day;
    }

    return year + "-" + month + "-" + day;
};

Application.prototype.cloneDate = function(date) {
    return new Date(date.getFullYear(), date.getMonth(), date.getDate());
};

Application.prototype.increaseDate = function(date) {
    return new Date(date.getFullYear(), date.getMonth(), date.getDate() + 1);
};
