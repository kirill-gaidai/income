<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:url var="stylesUrl" value="/css/styles.css"/>
<spring:url var="jQueryUrl" value="/js/jquery-3.2.1.js"/>
<spring:url var="scriptUrl" value="/js/summary.js"/>
<spring:url var="appCtx" value="/"/>

<!DOCTYPE html>
<html>
<head>
    <title>Summary</title>
    <link rel="stylesheet" type="text/css" href="${stylesUrl}"/>
    <script src="${jQueryUrl}"></script>
    <script>
        var appCtx = "${appCtx}".substr(0, "${appCtx}".length - 1);
    </script>
</head>
<body>

<h1>Summary</h1>

<table class="form">
    <tr>
        <td>Currency</td>
        <td><select id="currencies"></select></td>
        <td>First day</td>
        <td><input type="text" id="first_day"></td>
        <td>Last day</td>
        <td><input type="text" id="last_day"></td>
        <td><button id="filter"></button></td>
    </tr>
</table>

<hr>

<script src="${scriptUrl}"></script>
</body>
</html>
