<%@page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>
<head>
    <title>income</title>
    <style type="text/css">
        table, td {
            border: 1px solid black;
            border-collapse: collapse;
        }
    </style>
</head>
<body>

<h1>Accounts</h1>

<spring:url var="newAccountUri" value="/account/edit/new"/>

<a href="${newAccountUri}">New</a>

<table>
    <tr>
        <td>ID</td>
        <td>Currency</td>
        <td>Title</td>
        <td>Actions</td>
    </tr>
    <c:forEach var="account" items="${accounts}">

        <spring:url var="accountEditUri" value="/account/edit/${account.id}"/>
        <spring:url var="accountDeleteUri" value="/account/delete/${account.id}"/>
        <spring:url var="accountCardUri" value="/account/${account.id}/operation/edit/new"/>

        <tr>
            <td>${account.id}</td>
            <td>${account.currencyCode}</td>
            <td>
                <a href="${accountCardUri}">${account.title}</a>
            </td>
            <td>
                <a href="${accountEditUri}">Edit</a>
                <form method="post" action="${accountDeleteUri}">
                    <button>Delete</button>
                </form>
            </td>
        </tr>
    </c:forEach>
</table>

</body>
</html>
