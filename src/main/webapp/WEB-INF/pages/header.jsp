<%@page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
<head>
    <title>income</title>
    <spring:url var="stylesUrl" value="/css/styles.css"/>
    <link rel="stylesheet" type="text/css" href="${stylesUrl}"/>
</head>
<body>

<spring:url value="/" var="homeUrl"/>
<spring:url value="/category/list" var="categoriesUrl"/>
<spring:url value="/currency/list" var="currenciesUrl"/>
<spring:url value="/account/list" var="accountsUrl"/>
<a href="${homeUrl}">Go home</a>
<a href="${categoriesUrl}">Categories</a>
<a href="${currenciesUrl}">Currencies</a>
<a href="${accountsUrl}">Accounts</a>

<hr>
