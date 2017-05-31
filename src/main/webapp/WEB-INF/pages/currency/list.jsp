<%@page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:include page="/WEB-INF/pages/header.jsp"/>

<h1>Currencies</h1>
<spring:url var="newAccountUri" value="/currency/edit/new"/>
<a href="${newAccountUri}">New</a>
<table class="list">
    <tr>
        <td>ID</td>
        <td>Code</td>
        <td>Title</td>
        <td>Actions</td>
    </tr>
    <c:forEach var="currency" items="${currencies}">
        <tr>
            <td>${currency.id}</td>
            <td>${currency.code}</td>
            <td>${currency.title}</td>
            <td>
                <spring:url var="editUrl" value="/currency/edit/${currency.id}"/>
                <spring:url var="deleteUrl" value="/currency/delete/${currency.id}"/>
                <a href="${editUrl}">Edit</a>
                <form class="inline-form" method="post" action="${deleteUrl}">
                    <button>Delete</button>
                </form>
            </td>
        </tr>
    </c:forEach>
</table>

<jsp:include page="/WEB-INF/pages/footer.jsp"/>
