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
    let fullYear = (date.getFullYear() + "").padStart(4, "0");
    let month = (date.getMonth() + 1 + "").padStart(2, "0");
    let date = (date.getDate() + "").padStart(2, "0");
    return fullYear + "-" + month + "-" + date;
};
