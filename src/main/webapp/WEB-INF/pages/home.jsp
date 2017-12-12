<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:url var="stylesUrl" value="/css/styles.css"/>
<spring:url var="jQueryUrl" value="/js/jquery-3.2.1.js"/>
<spring:url var="applicationUrl" value="/js/application.js"/>
<spring:url var="appCtx" value="/"/>

<spring:url var="currenciesUrl" value="/currencies"/>
<spring:url var="categoriesUrl" value="/categories"/>
<spring:url var="accountsUrl" value="/accounts"/>
<spring:url var="summariesUrl" value="/summaries"/>

<!DOCTYPE html>
<html>
<head>
    <title>Income</title>
    <link rel="stylesheet" type="text/css" href="${stylesUrl}"/>
    <script src="${jQueryUrl}"></script>
    <script>
        let appCtx = "${appCtx}".substr(0, "${appCtx}".length - 1);
    </script>
</head>
<body>

<h1>Home page</h1>

<ul>
    <li><a href="${currenciesUrl}">Currencies</a></li>
    <li><a href="${categoriesUrl}">Categories</a></li>
    <li><a href="${accountsUrl}">Accounts</a></li>
    <li><a href="${summariesUrl}">Summaries</a></li>
</ul>

</body>
</html>
