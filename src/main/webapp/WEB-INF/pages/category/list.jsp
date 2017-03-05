<%@page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<spring:url var="newAccountUri" value="/category/edit/new"/>

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

<h1>Categories</h1>

<a href="${newAccountUri}">New</a>

<table>
    <tr>
        <td>ID</td>
        <td>Title</td>
        <td>Actions</td>
    </tr>
    <c:forEach var="category" items="${categories}">

        <spring:url var="editUrl" value="/category/edit/${category.id}"/>
        <spring:url var="deleteUrl" value="/category/delete/${category.id}"/>

        <tr>
            <td>${category.id}</td>
            <td>${category.title}</td>
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
