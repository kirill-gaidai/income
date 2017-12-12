"use strict";

document.addEventListener("DOMContentLoaded", function () {

    let formComponents = {
        cbFilterCurrencies: document.getElementById("cbFilterCurrencies"),
        edFilterFirstDay: document.getElementById("edFilterFirstDay"),
        edFilterLastDay: document.getElementById("edFilterLastDay"),
        btFilterSearch: document.getElementById("btFilterSearch"),

        lsAccounts: document.getElementById("lsAccounts"),
        lsCategories: document.getElementById("lsCategories"),

        fmBalance: document.getElementById("fmBalance"),
        cbBalanceAccount: document.getElementById("cbBalanceAccount"),
        edBalanceDay: document.getElementById("edBalanceDay"),
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
        edOperationNote: document.getElementById("edOperationNote"),
        btOperationSave: document.getElementById("btOperationSave"),
        btOperationCancel: document.getElementById("btOperationCancel"),

        lsOperations: document.getElementById("lsOperations"),
        btOperationsNew: document.getElementById("btOperationsNew"),
        btOperationsCancel: document.getElementById("btOperationsCancel"),

        lsSummaries: document.getElementById("lsSummaries")
    };

    let entity = null;
    let currencies = [];
    let day = null;

    formComponents.btFilterSearch.addEventListener("click", doOnBtFilterSearchClick);
    formComponents.lsSummaries.addEventListener("click", doOnLsSummariesClick);
    formComponents.btOperationsNew.addEventListener("click", doOnBtOperationsNewClick);
    formComponents.btBalanceCancel.addEventListener("click", doOnBtCancelClick);
    formComponents.btOperationsCancel.addEventListener("click", doOnBtCancelClick);
    formComponents.btOperationCancel.addEventListener("click", doOnBtCancelClick);
    formComponents.btBalanceSave.addEventListener("click", doOnBtBalanceSaveClick);
    formComponents.btOperationSave.addEventListener("click", doOnBtOperationSaveClick);

    doOnBtCancelClick();

    jQuery.getJSON(application.resourceUrls.currencies, data => {
        currencies = data;
        application.populateSelectOptions(formComponents.cbFilterCurrencies, data, "id", "title");
    });

    function doOnLsAccountsOrCategoriesCheck() {
        let accountIds = getCheckedAccounts();
        let categoryIds = getCheckedCategories();
        renderLsSummaries(transform(entity, Array.from(accountIds), Array.from(categoryIds)));
    }

    function doOnLsSummariesClick(event) {
        let target = event.target;
        if (this !== formComponents.lsSummaries || target.tagName !== "TD" || !target.node) {
            return;
        }
        if (target.node.type === "balance") {
            clearFmOperation();
            showFmBalance(target.node.balance);
            clearLsOperations();
            day = target.node.balance.day;
        }
        if (target.node.type === "operations") {
            clearFmOperation();
            clearFmBalance();
            showLsOperations(target.node.operations);
            day = target.node.day;
        }
    }

    function doOnBtFilterSearchClick() {
        let currencyId = formComponents.cbFilterCurrencies.value;
        let firstDay = formComponents.edFilterFirstDay.value;
        let lastDay = formComponents.edFilterLastDay.value;
        fetchSummaries(currencyId, firstDay, lastDay, false);
    }

    function doOnBtBalanceSaveClick() {
        let balance = {
            accountId: +formComponents.cbBalanceAccount.value,
            day: formComponents.edBalanceDay.value,
            amount: +formComponents.edBalanceAmount.value,
            manual: formComponents.chbBalanceManual.checked
        };
        jQuery.ajax({
            url: application.resourceUrls.balances,
            method: "post",
            data: JSON.stringify(balance),
            contentType: "application/json",
            dataType: "json",
            success: doOnPostSuccess,
            error: doOnError
        });
    }

    function doOnBtOperationSaveClick() {
        let id = formComponents.edOperationId.value;
        let operation = {
            accountId: +formComponents.cbOperationAccount.value,
            categoryId: +formComponents.cbOperationCategory.value,
            day: formComponents.edOperationDay.value,
            amount: +formComponents.edOperationAmount.value,
            note: formComponents.edOperationNote.value
        };
        if (id) {
            operation.id = +id;
        }
        jQuery.ajax({
            url: application.resourceUrls.operations,
            method: id ? "put" : "post",
            data: JSON.stringify(operation),
            contentType: "application/json",
            dataType: "json",
            success: doOnPostSuccess,
            error: doOnError
        });
    }

    function doOnLsOperationBtEditClick() {
        let operation = this.parentElement.parentElement.operation;
        clearLsOperations();
        clearFmBalance();
        showFmOperation(operation);
    }

    function doOnLsOperationBtDeleteClick() {
        let id = this.parentElement.parentElement.operation.id;
        jQuery.ajax({
            url: application.resourceUrls.operations + "/" + id,
            method: "delete",
            success: doOnPostSuccess,
            error: doOnError
        });
    }

    function doOnBtOperationsNewClick() {
        clearLsOperations();
        clearFmBalance();
        clearFmOperation(day);
    }

    function doOnBtCancelClick() {
        clearLsOperations();
        clearFmBalance();
        clearFmOperation();
    }

    function clearFmBalance() {
        formComponents.fmBalance.hidden = true;
        formComponents.cbBalanceAccount.value = null;
        formComponents.cbBalanceAccount.enabled = true;
        formComponents.edBalanceDay.value = "";
        formComponents.edBalanceDay.enabled = true;
        formComponents.edBalanceAmount.value = "";
        formComponents.chbBalanceManual.checked = false;
    }

    function showFmBalance(balanceEntity) {
        formComponents.fmBalance.hidden = false;
        formComponents.cbBalanceAccount.value = balanceEntity.accountId;
        formComponents.cbBalanceAccount.disabled = true;
        formComponents.edBalanceDay.value = application.dateToIsoStr(balanceEntity.day);
        formComponents.edBalanceDay.disabled = true;
        formComponents.edBalanceAmount.value = balanceEntity.amount;
        formComponents.chbBalanceManual.checked = balanceEntity.manual;
    }

    function clearFmOperation(day) {
        formComponents.fmOperation.hidden = arguments.length === 0;
        formComponents.edOperationId.value = "";
        formComponents.edOperationDay.value = arguments.length === 0 ? "" : application.dateToIsoStr(day);
        formComponents.cbOperationAccount.value = null;
        formComponents.cbOperationAccount.disabled = false;
        formComponents.cbOperationCategory.value = null;
        formComponents.edOperationAmount.value = "";
        formComponents.edOperationNote.value = "";
    }

    function showFmOperation(operationEntity) {
        formComponents.fmOperation.hidden = false;
        formComponents.edOperationId.value = operationEntity.id;
        formComponents.edOperationDay.value = application.dateToIsoStr(operationEntity.day);
        formComponents.cbOperationAccount.value = operationEntity.accountId;
        formComponents.cbOperationAccount.disabled = true;
        formComponents.cbOperationCategory.value = operationEntity.categoryId;
        formComponents.edOperationAmount.value = operationEntity.amount;
        formComponents.edOperationNote.value = operationEntity.note;
    }

    function clearLsOperations() {
        formComponents.lsOperations.hidden = true;
        let elem = formComponents.lsOperations.firstElementChild.firstElementChild;
        while (elem.children.length > 1) {
            elem.removeChild(elem.lastElementChild);
        }
    }

    function showLsOperations(operationList) {
        formComponents.lsOperations.hidden = false;
        let elem = formComponents.lsOperations.firstElementChild.firstElementChild;
        while (elem.children.length > 1) {
            elem.removeChild(elem.lastElementChild);
        }

        operationList.forEach(operation => {
            let rowElem = elem.appendChild(document.createElement("tr"));
            rowElem.operation = operation;

            let cellElem = rowElem.appendChild(document.createElement("td"));
            cellElem.innerText = operation.id;
            cellElem = rowElem.appendChild(document.createElement("td"));
            cellElem.innerText = application.dateToIsoStr(operation.day);
            cellElem = rowElem.appendChild(document.createElement("td"));
            cellElem.innerText = operation.accountTitle;
            cellElem = rowElem.appendChild(document.createElement("td"));
            cellElem.innerText = operation.categoryTitle;
            cellElem = rowElem.appendChild(document.createElement("td"));
            cellElem.innerText = operation.amount;
            cellElem = rowElem.appendChild(document.createElement("td"));
            cellElem.innerText = operation.note;
            cellElem = rowElem.appendChild(document.createElement("td"));

            let btElem = cellElem.appendChild(document.createElement("button"));
            btElem.innerText = "Edit";
            btElem.addEventListener("click", doOnLsOperationBtEditClick);
            btElem = cellElem.appendChild(document.createElement("button"));
            btElem.innerText = "Delete";
            btElem.addEventListener("click", doOnLsOperationBtDeleteClick);
        });
    }

    // ----- Summaries list -----

    function fetchSummaries(currencyId, firstDay, lastDay, refresh) {
        jQuery.getJSON(application.resourceUrls.summaries, {
            currency_id: +currencyId,
            first_day: firstDay,
            last_day: lastDay
        }, data => {
            data.firstDay = application.isoStrToDate(data.firstDay);
            data.lastDay = application.isoStrToDate(data.lastDay);
            data.initialBalances.forEach(balance => balance.day = application.isoStrToDate(balance.day));
            data.balances.forEach(balance => balance.day = application.isoStrToDate(balance.day));
            data.operations.forEach(operation => operation.day = application.isoStrToDate(operation.day));

            // filling combo boxes
            application.populateSelectOptions(formComponents.cbOperationCategory, data.categories, "id", "title");
            application.populateSelectOptions(formComponents.cbOperationAccount, data.accounts, "id", "title");
            application.populateSelectOptions(formComponents.cbBalanceAccount, data.accounts, "id", "title");

            let accountIds = refresh
                ? getCheckedAccounts()
                : data.operations.reduce((set, operation) => set.add(operation.accountId), new Set());
            let categoryIds = refresh
                ? getCheckedCategories()
                : data.operations.reduce((set, operation) => set.add(operation.categoryId), new Set());

            renderAccountList(data.accounts);
            renderCategoryList(data.categories);
            setCheckedAccounts(accountIds);
            setCheckedCategories(categoryIds);

            renderLsSummaries(transform(data, Array.from(accountIds), Array.from(categoryIds)));

            entity = data;
            clearFmBalance();
            clearFmOperation();
            clearLsOperations();
        });
    }

    function renderLsSummaries(model) {
        formComponents.lsSummaries.innerHTML = "";
        let tableElem = formComponents.lsSummaries.appendChild(document.createElement("table"));
        tableElem.className = "list";

        let bodyElem = tableElem.appendChild(document.createElement("tbody"));

        let rowElem = bodyElem.appendChild(document.createElement("tr"));

        let cellElem = rowElem.appendChild(document.createElement("th"));
        cellElem.innerText = "Day";

        cellElem = rowElem.appendChild(document.createElement("th"));
        cellElem.innerText = "Difference";

        model.accountTitles.forEach(title => {
            let cellElem = rowElem.appendChild(document.createElement("th"));
            cellElem.innerText = title;
        });

        cellElem = rowElem.appendChild(document.createElement("th"));
        cellElem.innerText = "Sum";

        model.categoryTitles.forEach(title => {
            let cellElem = rowElem.appendChild(document.createElement("th"));
            cellElem.innerText = title;
        });

        cellElem = rowElem.appendChild(document.createElement("th"));
        cellElem.innerText = "Sum";

        model.rows.forEach(row => {
            let rowElem = bodyElem.appendChild(document.createElement("tr"));

            let cellElem = rowElem.appendChild(document.createElement("td"));
            cellElem.innerText = application.dateToIsoStr(row.day);

            cellElem = rowElem.appendChild(document.createElement("td"));
            cellElem.innerText = row.difference;

            row.balanceAmounts.forEach(node => {
                let cellElem = rowElem.appendChild(document.createElement("td"));
                cellElem.innerText = node.balance.amount;
                cellElem.node = node;
            });

            cellElem = rowElem.appendChild(document.createElement("td"));
            cellElem.innerText = row.balanceAmountsSum;

            row.operationAmounts.forEach(node => {
                let cellElem = rowElem.appendChild(document.createElement("td"));
                cellElem.innerText = node.sum;
                cellElem.node = node;
            });

            cellElem = rowElem.appendChild(document.createElement("td"));
            cellElem.innerText = row.operationAmountsSum;
        });
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
        let balanceAmounts = accountIds.reduce((array, accountId) => {
            array.push({
                value: 0,
                balance: {accountId: accountId, day: entity.firstDay, amount: 0, manual: false}
            });
            return array;
        }, []);
        entity.initialBalances.reduce((array, balance) => {
            if (accountMap.has(balance.accountId)) {
                let node = array[accountMap.get(balance.accountId)];
                node.value = balance.amount;
                node.balance.amount = balance.amount;
                node.balance.manual = balance.manual;
            }
            return array;
        }, balanceAmounts);

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

        for (let day = application.cloneDate(entity.firstDay); day <= entity.lastDay; day = application.increaseDate(day)) {
            let resultRow = {};
            resultRow.day = day;
            resultRow.difference = 0;
            result.rows.push(resultRow);

            let timestamp = +day;
            populateRowBalances(resultRow, accountMap,
                balanceMap.has(timestamp) ? balanceMap.get(timestamp) : new Map(), balanceAmounts, day);
            populateRowOperations(resultRow, accountMap, categoryMap,
                operationMap.has(timestamp) ? operationMap.get(timestamp) : new Map(), day);
        }

        return result;

        function populateRowBalances(row, accountMap, balanceDayMap, balanceAmounts, day) {
            row.difference = -balanceAmounts.reduce((sum, node) => sum + node.balance.amount, 0);
            // creating default balances for this day
            balanceAmounts.forEach((node, index, array) => {
                array[index] = {
                    type: "balance",
                    balance: {accountId: node.balance.accountId, day: day, amount: node.balance.amount, manual: false}
                }
            });
            // setting balances if they exist for day and account, otherwise default balances stay
            accountMap.forEach((columnId, accountId) => {
                if (balanceDayMap.has(accountId)) {
                    balanceAmounts[columnId].balance = balanceDayMap.get(accountId);
                }
            });

            row.balanceAmounts = balanceAmounts.slice();
            row.balanceAmountsSum = balanceAmounts.reduce((sum, node) => sum + node.balance.amount, 0);
            row.difference += row.balanceAmountsSum;
        }

        function populateRowOperations(row, accountMap, categoryMap, operationDayMap, day) {
            row.operationAmountsSum = 0;
            row.operationAmounts = [];
            categoryMap.forEach(() => row.operationAmounts.push({
                type: "operations",
                sum: 0,
                day: day,
                operations: []
            }));

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

    // ----- Accounts checkbox list -----

    function renderAccountList(accountList) {
        formComponents.lsAccounts.innerHTML = "";
        accountList.forEach(account => {
            let labelElem = formComponents.lsAccounts.appendChild(document.createElement("label"));
            let checkBoxElem = labelElem.appendChild(document.createElement("input"));
            checkBoxElem.type = "checkbox";
            checkBoxElem.value = account.id;
            checkBoxElem.checked = false;
            checkBoxElem.addEventListener("change", doOnLsAccountsOrCategoriesCheck);
            labelElem.appendChild(document.createTextNode(account.title));
        });
    }

    function getCheckedAccounts() {
        let result = new Set();
        for (let elem = formComponents.lsAccounts.firstElementChild; elem; elem = elem.nextElementSibling) {
            let chbAccount = elem.firstElementChild;
            if (chbAccount.checked) {
                result.add(+chbAccount.value);
            }
        }
        return result;
    }

    function setCheckedAccounts(accountIds) {
        for (let elem = formComponents.lsAccounts.firstElementChild; elem; elem = elem.nextElementSibling) {
            let chbAccount = elem.firstElementChild;
            chbAccount.checked = accountIds.has(+chbAccount.value);
        }
    }

    // ----- Categories checkbox list -----

    function renderCategoryList(categoryList) {
        formComponents.lsCategories.innerHTML = "";
        categoryList.forEach(category => {
            let labelElem = formComponents.lsCategories.appendChild(document.createElement("label"));
            let checkBoxElem = labelElem.appendChild(document.createElement("input"));
            checkBoxElem.type = "checkbox";
            checkBoxElem.value = category.id;
            checkBoxElem.checked = false;
            checkBoxElem.addEventListener("change", doOnLsAccountsOrCategoriesCheck);
            labelElem.appendChild(document.createTextNode(category.title));
        });
    }

    function getCheckedCategories() {
        let result = new Set();
        for (let elem = formComponents.lsCategories.firstElementChild; elem; elem = elem.nextElementSibling) {
            let chbCategory = elem.firstElementChild;
            if (chbCategory.checked) {
                result.add(+chbCategory.value);
            }
        }
        return result;
    }

    function setCheckedCategories(categoryIds) {
        for (let elem = formComponents.lsCategories.firstElementChild; elem; elem = elem.nextElementSibling) {
            let chbCategory = elem.firstElementChild;
            chbCategory.checked = categoryIds.has(+chbCategory.value);
        }
    }

    // ----- General -----

    function doOnError() {
        console.log("error");
    }

    function doOnPostSuccess() {
        fetchSummaries(entity.currency.id,
            application.dateToIsoStr(entity.firstDay), application.dateToIsoStr(entity.lastDay), true);
    }

});
