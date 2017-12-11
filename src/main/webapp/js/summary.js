"use strict";

document.addEventListener("DOMContentLoaded", function() {

    let formComponents = {
        cbFilterCurrencies: document.getElementById("cbFilterCurrencies"),
        edFilterFirstDay: document.getElementById("edFilterFirstDay"),
        edFilterLastDay: document.getElementById("edFilterLastDay"),
        btFilterSearch: document.getElementById("btFilterSearch"),
        lsAccounts: document.getElementById("lsAccounts"),
        lsCategories: document.getElementById("lsCategories"),

        fmBalance: document.getElementById("fmBalance"),
        cbBalanceAccount: document.getElementById("cbBalanceAccount"),
        edBalanceDay: document.getElementById("edBalanceAmount"),
        edBalanceAmount: document.getElementById("edBalanceAmount"),
        chbBalanceManual: document.getElementById("chbBalanceManual"),
        btBalanceSave: document.getElementById("btBalanceSave"),
        btBalanceCancel: document.getElementById("btBalanceCancel"),

        fmOperation: document.getElementById("fmOperation"),
        edOperationId: document.getElementById("edOperationId"),
        edOperationDay: document.getElementById("edOperationDay"),
        cbOperationAccount: document.getElementById("cbOperationAccount"),
        cbOperationCategory: document.getElementById("cbOperationCategory"),
        edOperationAmount: document.getElementById("edOperationAmount"),

        container: document.getElementById("container")
    };

    let entity = null;
    let currencies = [];
    formComponents.btFilterSearch.addEventListener("click", doOnBtFilterSearchClick);
    jQuery.getJSON(application.resourceUrls.currencies, data => {
        currencies = data;
        application.populateSelectOptions(formComponents.cbFilterCurrencies, data, "id", "title");
    });

    function doOnAccountsOrCategoryCheck() {
        let accountIds = getCheckedIds(formComponents.lsAccounts);
        let categoryIds = getCheckedIds(formComponents.lsCategories);
        render(transform(entity, accountIds, categoryIds));

        function getCheckedIds(elem) {
            let result = [];
            let children = elem.children;
            for (let index = 0; index < children.length; index++) {
                let checkbox = children[index].firstElementChild;
                if (checkbox.checked) {
                    result.push(+checkbox.value);
                }
            }
            return result;
        }
    }

    function doOnBtFilterSearchClick() {
        let currencyId = formComponents.cbFilterCurrencies.value;
        let firstDay = formComponents.edFilterFirstDay.value;
        let lastDay = formComponents.edFilterLastDay.value;
        jQuery.getJSON(application.resourceUrls.summaries, {
            currency_id: +currencyId,
            first_day: firstDay,
            last_day: lastDay
        }, data => {
            entity = data;
            entity.firstDay = application.isoStrToDate(entity.firstDay);
            entity.lastDay = application.isoStrToDate(entity.lastDay);
            entity.initialBalances.forEach(balance => balance.day = application.isoStrToDate(balance.day));
            entity.balances.forEach(balance => balance.day = application.isoStrToDate(balance.day));
            entity.operations.forEach(operation => operation.day = application.isoStrToDate(operation.day));

            // filling combo boxes
            application.populateSelectOptions(formComponents.cbOperationCategory, data.categories, "id", "title");
            application.populateSelectOptions(formComponents.cbOperationAccount, data.accounts, "id", "title");
            application.populateSelectOptions(formComponents.cbBalanceAccount, data.accounts, "id", "title");

            let accountIds = [];
            let categoryIds = [];
            entity.operations.forEach(operation => {
                if (accountIds.indexOf(operation.accountId) === -1) {
                    accountIds.push(operation.accountId);
                }
                if (categoryIds.indexOf(operation.categoryId) === -1) {
                    categoryIds.push(operation.categoryId);
                }
            });
            accountIds.sort();
            categoryIds.sort();

            renderList(formComponents.lsAccounts, entity.accounts, accountIds);
            renderList(formComponents.lsCategories, entity.categories, categoryIds);
            render(transform(entity, accountIds, categoryIds));

            function renderList(elem, list, checked) {
                elem.innerHTML = "";
                list.forEach(item => {
                    let labelElem = elem.appendChild(document.createElement("label"));
                    let checkBoxElem = labelElem.appendChild(document.createElement("input"));
                    checkBoxElem.type = "checkbox";
                    checkBoxElem.value = item.id;
                    checkBoxElem.checked = checked.indexOf(item.id) !== -1;
                    checkBoxElem.addEventListener("change", doOnAccountsOrCategoryCheck);
                    labelElem.appendChild(document.createTextNode(item.title));
                });
            }
        });
    }

    function render(model) {
        formComponents.container.innerHTML = "";
        let tbodyElem = formComponents.container.appendChild(document.createElement("table")).appendChild(document.createElement("tbody"))
        let rowElem = tbodyElem.appendChild(document.createElement("tr"));
        rowElem.appendChild(document.createElement("th")).innerText = "Day";
        rowElem.appendChild(document.createElement("th")).innerText = "Difference";
        model.accountTitles.forEach(title => rowElem.appendChild(document.createElement("th")).innerText = title);
        rowElem.appendChild(document.createElement("th")).innerText = "Sum";
        model.categoryTitles.forEach(title => rowElem.appendChild(document.createElement("th")).innerText = title);
        rowElem.appendChild(document.createElement("th")).innerText = "Sum";
        model.rows.forEach(row => {
            let rowElem = tbodyElem.appendChild(document.createElement("tr"));
            rowElem.appendChild(document.createElement("td")).innerText = formatDate(row.day);
            rowElem.appendChild(document.createElement("td")).innerText = row.difference;
            row.balanceAmounts.forEach(amount => rowElem.appendChild(document.createElement("td")).innerText = amount);
            rowElem.appendChild(document.createElement("td")).innerText = row.balanceAmountsSum;
            row.operationAmounts.forEach(node => {
                let elem = rowElem.appendChild(document.createElement("td"));
                elem.innerText = node.sum;
                elem.operations = node.operations;
            });
            rowElem.appendChild(document.createElement("td")).innerText = row.operationAmountsSum;
        });

        function formatDate(day) {
            return day.toLocaleString("ru", { year: "numeric", month: "numeric", day: "numeric" });
        }
    }

    function transform(entity, accountIds, categoryIds) {
        // initializing account id -> column index map
        let accountMap = accountIds.reduce((map, accountId, columnIndex) => map.set(accountId, columnIndex), new Map());

        // initializing category id -> column index map
        let categoryMap = categoryIds.reduce((map, categoryId, columnIndex) => map.set(categoryId, columnIndex), new Map());

        // initializing balances map
        let balanceMap = entity.balances.reduce((map, balance) => {
            getChildStructure(map, +balance.day, Map).set(balance.accountId, balance);
            return map;
        }, new Map());

        // initializing operations map
        let operationMap = entity.operations.reduce((map, operation) => {
            getChildStructure(getChildStructure(getChildStructure(map,
                +operation.day, Map), operation.accountId, Map), operation.categoryId, Array).push(operation);
            return map;
        }, new Map());

        // balance amounts before first day
        let balanceAmounts = (new Array(accountMap.size)).fill(0);
        entity.initialBalances.forEach(initialBalance => {
            if (accountMap.has(initialBalance.accountId)) {
                balanceAmounts[accountMap.get(initialBalance.accountId)] = initialBalance.amount;
            }
        });

        let result = {
            accountTitles: (new Array(accountMap.size)).fill("N/A"),
            categoryTitles: (new Array(categoryMap.size)).fill("N/A"),
            rows: []
        };

        entity.accounts.forEach(account => {
            if (accountMap.has(account.id)) {
                result.accountTitles[accountMap.get(account.id)] = account.title;
            }
        });

        entity.categories.forEach(category => {
            if (categoryMap.has(category.id)) {
                result.categoryTitles[categoryMap.get(category.id)] = category.title;
            }
        });

        for (let day = new Date(+entity.firstDay); day <= entity.lastDay; day.setDate(day.getDate() + 1)) {
            let resultRow = {};
            result.rows.push(resultRow);

            let timestamp = +day;
            resultRow.day = new Date(timestamp);
            resultRow.difference = 0;
            populateRowBalances(resultRow, accountMap,
                balanceMap.has(timestamp) ? balanceMap.get(timestamp) : new Map(), balanceAmounts);
            populateRowOperations(resultRow, accountMap, categoryMap,
                operationMap.has(timestamp) ? operationMap.get(timestamp) : new Map());
        }

        return result;

        function populateRowBalances(row, accountMap, balanceDayMap, balanceAmounts) {
            let prevBalanceAmountsSum = balanceAmounts.reduce((sum, amount) => sum + amount, 0);
            accountMap.forEach((columnId, accountId) => {
                if (balanceDayMap.has(accountId)) {
                    balanceAmounts[columnId] = balanceDayMap.get(accountId).amount;
                }
            });
            let balanceAmountsSum = balanceAmounts.reduce((sum, amount) => sum + amount, 0);
            row.balanceAmounts = balanceAmounts.slice();
            row.balanceAmountsSum = balanceAmountsSum;
            row.difference += balanceAmountsSum - prevBalanceAmountsSum;
        }

        function populateRowOperations(row, accountMap, categoryMap, operationDayMap) {
            row.operationAmounts = [];
            for (let index = 0; index < categoryMap.size; index++) {
                row.operationAmounts.push({
                    sum: 0,
                    operations: []
                })
            }

            row.operationAmountsSum = 0;
            row.difference = 0;
            accountMap.forEach((accountColumnId, accountId) => {
                let operationDayAccountMap = getChildStructure(operationDayMap, accountId);
                if (operationDayAccountMap === null) {
                    return;
                }

                operationDayAccountMap.forEach((operationArray, categoryId) => {
                    let sum = operationArray.reduce((sum, operation) => sum + operation.amount, 0);
                    row.difference += sum;

                    // if category is displayed
                    if (categoryMap.has(categoryId)) {
                        row.operationAmountsSum += sum;
                        let node = row.operationAmounts[categoryMap.get(categoryId)];
                        node.sum += sum;
                        Array.prototype.push.apply(node.operations, operationArray);
                    }
                });
            });
        }

        function getChildStructure(parentMap, key, childProto) {
            if (parentMap.has(key)) {
                return parentMap.get(key);
            }
            if (arguments.length === 2) {
                return null;
            }
            let result = new childProto();
            parentMap.set(key, result);
            return result;
        }
    }

});
