<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:url var="stylesUrl" value="/css/styles.css"/>
<spring:url var="jQueryUrl" value="/js/jquery-3.2.1.js"/>
<spring:url var="applicationUrl" value="/js/application.js"/>
<spring:url var="appCtx" value="/"/>
<spring:url var="scriptUrl" value="/js/categories.js"/>

<!DOCTYPE html>
<html>
<head>
    <title>Categories</title>
    <link rel="stylesheet" type="text/css" href="${stylesUrl}"/>
    <script src="${jQueryUrl}"></script>
    <script>
        var appCtx = "${appCtx}".substr(0, "${appCtx}".length - 1);
    </script>
</head>
<body>

<h1>Categories</h1>

<form name="form" id="form">
    <table class="form">
        <tr>
            <td><label for="id">ID</label></td>
            <td><input type="text" name="id" id="id" readonly></td>
        </tr>
        <tr>
            <td><label for="sort">Sort</label></td>
            <td><input type="text" name="sort" id="sort"></td>
        </tr>
        <tr>
            <td><label for="title">Title</label></td>
            <td><input type="text" name="title" id="title"></td>
        </tr>
        <tr>
            <td></td>
            <td>
                <input type="submit" name="new" id="new" value="New">
                <input type="submit" name="save" id="save" value="Save">
            </td>
        </tr>
    </table>
</form>

<table class="list" id="list">
    <tr>
        <th>ID</th>
        <th>Sort</th>
        <th>Title</th>
        <th>Actions</th>
    </tr>
</table>

<script src="${scriptUrl}"></script>
</body>
