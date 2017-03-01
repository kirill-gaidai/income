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

<table>
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

</body>
</html>
