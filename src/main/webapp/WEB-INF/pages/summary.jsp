<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:url var="stylesUrl" value="/css/styles.css"/>
<spring:url var="jQueryUrl" value="/js/jquery-3.2.1.js"/>
<spring:url var="applicationUrl" value="/js/application.js"/>
<spring:url var="scriptUrl" value="/js/summary.js"/>
<spring:url var="appCtx" value="/"/>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Summary</title>
    <link rel="stylesheet" type="text/css" href="${stylesUrl}"/>
    <script src="${jQueryUrl}"></script>
    <script>
        "use strict";
        let appCtx = "${appCtx}".substr(0, "${appCtx}".length - 1);
    </script>
    <script src="${applicationUrl}"></script>
</head>
<body>
<div>
    <label>Currency: <select id="cbFilterCurrencies"></select></label>
    <label>First day: <input id="edFilterFirstDay" type="text" value="2017-12-01"></label>
    <label>Last day: <input id="edFilterLastDay" type="text" value="2017-12-05"></label>
    <button id="btFilterSearch">Search</button>
</div>
<hr>
<div id="lsAccounts"></div>
<hr>
<div id="lsCategories"></div>
<hr>

<div id="fmBalance">
    <table class="form">
        <tr>
            <td><label for="cbBalanceAccount">Account</label></td>
            <td><select id="cbBalanceAccount"></select></td>
        </tr>
        <tr>
            <td><label for="edBalanceDay">Day</label></td>
            <td><input type="text" id="edBalanceDay"></td>
        </tr>
        <tr>
            <td><label for="edBalanceAmount">Amount</label></td>
            <td><input type="text" id="edBalanceAmount"></td>
        </tr>
        <tr>
            <td></td>
            <td><label><input type="checkbox" id="chbBalanceManual">Manual</label></td>
        </tr>
        <tr>
            <td></td>
            <td>
                <button id="btBalanceSave">Save</button>
                <button id="btBalanceCancel">Cancel</button>
            </td>
        </tr>
    </table>
</div>

<div id="fmOperation">
    <table class="form">
        <tr>
            <td><label for="edOperationId">ID</label></td>
            <td><input type="text" id="edOperationId" readonly></td>
        </tr>
        <tr>
            <td><label for="edOperationDay">Day</label></td>
            <td><input type="text" id="edOperationDay"></td>
        </tr>
        <tr>
            <td><label for="cbOperationAccount">Account</label></td>
            <td><select id="cbOperationAccount"></select></td>
        </tr>
        <tr>
            <td><label for="cbOperationCategory">Category</label></td>
            <td><select id="cbOperationCategory"></select></td>
        </tr>
        <tr>
            <td><label for="edOperationAmount">Amount</label></td>
            <td><input type="text" id="edOperationAmount"></td>
        </tr>
        <tr>
            <td><label for="edOperationNote"></label></td>
            <td><textarea id="edOperationNote"></textarea></td>
        </tr>
        <tr>
            <td></td>
            <td>
                <button id="btOperationNew">New</button>
                <button id="btOperationSave">Save</button>
                <button id="btOperationCancel">Cancel</button>
            </td>
        </tr>
    </table>
</div>

<div id="lsOperations">
    <table class="list">
        <tr>
            <th>ID</th>
            <th>Day</th>
            <th>Account</th>
            <th>Category</th>
            <th>Amount</th>
            <th>Note</th>
            <th>Actions</th>
        </tr>
    </table>
</div>

<hr>

<div id="lsSummaries"></div>

<script src="${scriptUrl}"></script>
</body>
</html>
