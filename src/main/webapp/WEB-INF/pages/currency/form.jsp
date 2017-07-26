<%@page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:include page="/WEB-INF/pages/header.jsp"/>

<h1>Currency</h1>
<spring:url value="/currency/edit" var="actionUrl"/>
<form:form method="post" action="${actionUrl}" modelAttribute="currencyDto">
    <form:hidden path="id"/>
    <table class="form">
        <tr>
            <td><form:label path="code">Code</form:label></td>
            <td><form:input path="code"/></td>
        </tr>
        <tr>
            <td><form:label path="title">Title</form:label></td>
            <td><form:input path="title"/></td>
        </tr>
        <tr>
            <td><form:label path="accuracy">Accuracy</form:label></td>
            <td><form:input path="accuracy"/></td>
        </tr>
        <tr>
            <td></td>
            <td><form:button>Save</form:button></td>
        </tr>
    </table>
</form:form>

<jsp:include page="/WEB-INF/pages/footer.jsp"/>
