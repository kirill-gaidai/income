<%@page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<spring:url var="newUrl" value="/currency/edit/new"/>

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

<h1>Currencies</h1>

<a href="${newUrl}">New</a>

<table>
    <tr>
        <td>ID</td>
        <td>Code</td>
        <td>Title</td>
        <td>Actions</td>
    </tr>
    <c:forEach var="currency" items="${currencies}">

        <spring:url var="editUrl" value="/currency/edit/${currency.id}"/>
        <spring:url var="deleteUrl" value="/currency/delete/${currency.id}"/>

        <tr>
            <td>${currency.id}</td>
            <td>${currency.code}</td>
            <td>${currency.title}</td>
            <td>
                <a href="${editUrl}">Edit</a>
                <form method="post" action="${deleteUrl}">
                    <button>Delete</button>
                </form>
            </td>
        </tr>
    </c:forEach>
</table>

</body>
</html>
