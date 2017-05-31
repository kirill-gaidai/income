<%@page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:include page="/WEB-INF/pages/header.jsp"/>

<h1>Accounts</h1>
<spring:url var="newAccountUri" value="/account/edit/new"/>
<a href="${newAccountUri}">New</a>
<table class="list">
    <tr>
        <td>ID</td>
        <td>Currency</td>
        <td>Sort</td>
        <td>Title</td>
        <td>Actions</td>
    </tr>
    <c:forEach var="account" items="${accounts}">
        <tr>
            <td>${account.id}</td>
            <td>${account.currencyCode}</td>
            <td>${account.sort}</td>
            <td>${account.title}</td>
            <td>
                <spring:url var="accountEditUri" value="/account/edit/${account.id}"/>
                <spring:url var="accountDeleteUri" value="/account/delete/${account.id}"/>
                <a href="${accountEditUri}">Edit</a>
                <form class="inline-form" method="post" action="${accountDeleteUri}">
                    <button>Delete</button>
                </form>
            </td>
        </tr>
    </c:forEach>
</table>

<jsp:include page="/WEB-INF/pages/footer.jsp"/>
