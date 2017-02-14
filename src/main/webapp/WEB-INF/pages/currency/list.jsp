<%@page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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

<table>
    <tr>
        <td>ID</td>
        <td>Code</td>
        <td>Title</td>
    </tr>
    <c:forEach var="currency" items="${currencies}">
        <tr>
            <td>${currency.id}</td>
            <td>${currency.code}</td>
            <td>${currency.title}</td>
        </tr>
    </c:forEach>
</table>

</body>
</html>
