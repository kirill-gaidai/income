<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:url var="stylesUrl" value="/css/styles.css"/>
<spring:url var="jQueryUrl" value="/js/jquery-3.2.1.js"/>
<spring:url var="scriptUrl" value="/js/operations.js"/>
<spring:url var="appCtx" value="/"/>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Operations list</title>
    <link rel="stylesheet" type="text/css" href="${stylesUrl}"/>
    <script src="${jQueryUrl}"></script>
    <script>
        var appCtx = "${appCtx}".substr(0, "${appCtx}".length - 1);
    </script>
</head>
<body>

<h1>Operations list</h1>

<hr>

<table class="form">
    <tr>
        <td><label for="edId">ID</label></td>
        <td><input type="text" id="edId" readonly></td>
    </tr>
    <tr>
        <td><label for="cbAccount">Account</label></td>
        <td><select id="cbAccount"></select></td>
    </tr>
    <tr>
        <td><label for="cbCategory">Category</label></td>
        <td><select id="cbCategory"></select></td>
    </tr>
    <tr>
        <td><label for="edDay">Day</label></td>
        <td><input type="text" id="edDay"></td>
    </tr>
    <tr>
        <td><label for="edAmount">Amount</label></td>
        <td><input type="text" id="edAmount"></td>
    </tr>
    <tr>
        <td><label for="edNote">Note</label></td>
        <td><textarea id="edNote"></textarea></td>
    </tr>
    <tr>
        <td></td>
        <td>
            <button id="btNew">New</button>
            <button id="btSave">Save</button>
        </td>
    </tr>
</table>

<hr>

<table class="form">
    <tr>
        <td><label for="cbCurrencyId">Currency</label></td>
        <td><select id="cbCurrencyId"></select></td>
    </tr>
    <tr>
        <td><label for="lsAccounts">Accounts</label></td>
        <td><select multiple id="lsAccounts"></select></td>
    </tr>
    <tr>
        <td><label for="edFirstDay">First day</label></td>
        <td><input type="text" id="edFirstDay"></td>
    </tr>
    <tr>
        <td><label for="edLastDay">Last day</label></td>
        <td><input type="text" id="edLastDay"></td>
    </tr>
    <tr>
        <td></td>
        <td><button id="btFilter">Filter</button></td>
    </tr>
</table>

<hr>

<table class="list" id="lsOperations">
    <tr>
        <th>ID</th>
        <th>Account</th>
        <th>Category</th>
        <th>Day</th>
        <th>Amount</th>
        <th>Note</th>
        <th>Actions</th>
    </tr>
</table>

<script src="${scriptUrl}"></script>
</body>
