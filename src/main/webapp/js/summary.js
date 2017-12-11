"use strict";

var entityList;

var formComponents = {
    cbCurrencies: document.getElementById("currencies"),
    edFirstDay: document.getElementById("first_day"),
    edLastDay: document.getElementById("last_day")
};

var resourceUrls = {
    summaries: appCtx + "/rest/summary"
}

function doGetList() {
    jQuery.getJSON(resourceUrls.summaries, function callback(data) {
        entityList = data;
    });
}

function transform(entity, accountIds, categoryIds) {
    var accountMap = new Map();
    accountIds.forEach(function (accountId, index) {
        accountMap.put(accountId, index);
    });

    var categoryMap = new Map();
    categoryIds.forEach(function (categoryId, index) {
        categoryMap.push(categoryId, index);
    });

    var operationsMap = new Map();
    entity.operations.forEach(function (operation) {
        var daysMap;
        if (operationsMap.has(operation.day)) {
            daysMap = operationsMap.get(operation.day);
        } else {
            daysMap = new Map();
            operationsMap.put(operation.day, daysMap);
        }

        var categoryIdNode;
        if (daysMap.has(operation.categoryId)) {
            categoryIdNode = daysMap.get(operation.categoryId);
        } else {
            categoryIdNode = {
                amount: 0,
                list: []
            };
            daysMap.put(operation.categoryId, categoryIdsMap);
        }

        categoryIdNode.amount += operation.amount;
        categoryIdNode.push(operation);
    });

    var balancesMap = new Map();
    entity.
}