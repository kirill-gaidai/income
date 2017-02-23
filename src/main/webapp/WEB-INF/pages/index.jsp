<%@page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<spring:url value="/category/list" var="categoriesUrl"/>
<spring:url value="/currency/list" var="currenciesUrl"/>
<spring:url value="/account/list" var="accountsUrl"/>

<!DOCTYPE html>
<html>
<head>
    <title>income</title>
</head>
<body>
<a href="${categoriesUrl}">Categories</a>
<a href="${currenciesUrl}">Currencies</a>
<a href="${accountsUrl}">Accounts</a>
</body>
</html>
