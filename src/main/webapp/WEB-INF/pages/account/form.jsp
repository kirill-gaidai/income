<%@page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<jsp:include page="/WEB-INF/pages/header.jsp"/>

<h1>Account</h1>
<spring:url value="/account/edit" var="actionUrl"/>
<form:form method="post" action="${actionUrl}" modelAttribute="account">
    <form:hidden path="id"/>
    <table class="form">
        <tr>
            <td><form:label path="sort">Sort</form:label></td>
            <td><form:input path="sort"/></td>
        </tr>
        <tr>
            <td><form:label path="title">Title</form:label></td>
            <td><form:input path="title"/></td>
        </tr>
        <tr>
            <td><form:label path="currencyId">Currency</form:label></td>
            <td><form:select path="currencyId" items="${currencies}" itemValue="id" itemLabel="title"/></td>
        </tr>
        <tr>
            <td></td>
            <td><form:button>Save</form:button></td>
        </tr>
    </table>
</form:form>

<jsp:include page="/WEB-INF/pages/footer.jsp"/>
