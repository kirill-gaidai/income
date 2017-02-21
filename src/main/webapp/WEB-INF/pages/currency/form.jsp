<%@page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<spring:url value="/currency/edit" var="actionUrl"/>

<!DOCTYPE html>
<html>
<head>
    <title>income</title>
    <style type="text/css">
        table, td {
            border: none;
            border-collapse: collapse;
        }
    </style>
</head>
<body>

<h1>Currency</h1>

<form:form method="post" action="${actionUrl}" modelAttribute="currencyDto">
    <form:hidden path="id"/>
    <table>
        <tr>
            <td><form:label path="code">Code</form:label></td>
            <td><form:input path="code"/></td>
        </tr>
        <tr>
            <td><form:label path="title">Title</form:label></td>
            <td><form:input path="title"/></td>
        </tr>
        <tr>
            <td></td>
            <td><form:button>Save</form:button></td>
        </tr>
    </table>
</form:form>

</body>
</html>
