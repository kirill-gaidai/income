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

<spring:url var="newUrl" value="/account/edit/new"/>

<a href="${newUrl}">New</a>

<table>
    <tr>
        <td>ID</td>
        <td>Currency</td>
        <td>Title</td>
        <td>Actions</td>
    </tr>
    <c:forEach var="account" items="${accounts}">

        <spring:url var="accountEdit" value="/account/edit/${account.id}"/>
        <spring:url var="accountDelete" value="/account/delete/${account.id}"/>

        <tr>
            <td>${account.id}</td>
            <td>${account.currencyCode}</td>
            <td>${account.title}</td>
            <td>
                <a href="${accountEdit}">Edit</a>
                <form method="post" action="${accountDelete}">
                    <button>Delete</button>
                </form>
            </td>
        </tr>
    </c:forEach>
</table>

</body>
</html>
