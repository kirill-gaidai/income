<%@page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>
<head>
    <title>income</title>
    <spring:url var="stylesUri" value="/css/styles.css"/>
    <link rel="stylesheet" type="text/css" href="${stylesUri}"/>
</head>
<body>

<table class="list">
    <tr>
        <td>ID</td>
        <td>${account.id}</td>
    </tr>
    <tr>
        <td>Currency</td>
        <td>${account.currencyCode}</td>
    </tr>
    <tr>
        <td>Title</td>
        <td>${account.title}</td>
    </tr>
</table>

<spring:url value="/account/card/${operation.accountId}/operation" var="saveOperationUrl"/>

<form:form method="post" action="${saveOperationUrl}" modelAttribute="operation">
    <form:hidden path="accountId"/>
    <table class="form">
        <tr>
            <td><form:label path="categoryId">Category</form:label></td>
            <td><form:select path="categoryId" items="${categories}" itemValue="id" itemLabel="title"/></td>
        </tr>
        <tr>
            <td><form:label path="amount">Amount</form:label></td>
            <td><form:input path="amount"/></td>
        </tr>
        <tr>
            <td><form:label path="day">Day</form:label></td>
            <td><form:input path="day"/></td>
        </tr>
        <tr>
            <td><form:label path="note">Note</form:label></td>
            <td><form:textarea path="note"/></td>
        </tr>
        <tr>
            <td></td>
            <td><form:button>Save</form:button></td>
        </tr>
    </table>
</form:form>

</body>
</html>
