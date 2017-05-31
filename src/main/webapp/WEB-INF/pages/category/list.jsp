<%@page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:include page="/WEB-INF/pages/header.jsp"/>

<h1>Categories</h1>
<spring:url var="newAccountUri" value="/category/edit/new"/>
<a href="${newAccountUri}">New</a>
<table class="list">
    <tr>
        <td>ID</td>
        <td>Sort</td>
        <td>Title</td>
        <td>Actions</td>
    </tr>
    <c:forEach var="category" items="${categories}">
        <tr>
            <td>${category.id}</td>
            <td>${category.sort}</td>
            <td>${category.title}</td>
            <td>
                <spring:url var="editUrl" value="/category/edit/${category.id}"/>
                <spring:url var="deleteUrl" value="/category/delete/${category.id}"/>
                <a href="${editUrl}">Edit</a>
                <form class="inline-form" method="post" action="${deleteUrl}">
                    <button>Delete</button>
                </form>
            </td>
        </tr>
    </c:forEach>
</table>

<jsp:include page="/WEB-INF/pages/footer.jsp"/>
