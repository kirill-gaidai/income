<%@page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:include page="/WEB-INF/pages/header.jsp"/>

<h1>Operations</h1>
<spring:url value="/operation/edit" var="saveUrl"/>
<form:form method="post" action="${saveUrl}" modelAttribute="operationDto">

    <input type="hidden" name="return_first_day" value="${returnFirstDay}">
    <input type="hidden" name="return_last_day" value="${returnLastDay}">
    <c:forEach var="returnAccountId" items="${returnAccountIds}">
        <input type="hidden" name="return_account_id" value="${returnAccountId}">
    </c:forEach>

    <table class="form">
        <tr>
            <c:choose>
                <c:when test="${empty accountDtoList}">
                    <td>Account</td>
                    <td><form:hidden path="accountId"/>${operationDto.accountTitle}</td>
                </c:when>
                <c:otherwise>
                    <td><form:label path="accountId">Account</form:label></td>
                    <td>
                        <form:select path="accountId" items="${accountDtoList}" itemValue="id" itemLabel="title"/>
                    </td>
                </c:otherwise>
            </c:choose>
        </tr>
        <tr>
            <c:choose>
                <c:when test="${empty categoryDtoList}">
                    <td>Category</td>
                    <td><form:hidden path="categoryId"/>${operationDto.categoryTitle}</td>
                </c:when>
                <c:otherwise>
                    <td><form:label path="categoryId">Category</form:label></td>
                    <td>
                        <form:select path="categoryId" items="${categoryDtoList}" itemValue="id" itemLabel="title"/>
                    </td>
                </c:otherwise>
            </c:choose>
        </tr>
        <tr>
            <td><form:label path="amount">Amount</form:label></td>
            <td><form:input path="amount"/></td>
        </tr>
        <tr>
            <td>Day</td>
            <td><form:hidden path="day"/>${operationDto.day}</td>
        </tr>
        <tr>
            <td><form:label path="note">Note</form:label></td>
            <td><form:textarea path="note"/></td>
        </tr>
        <tr>
            <td></td>
            <td><form:button>Save</form:button></td>
        </tr>
    </table>
</form:form>

<hr/>

<table class="list">
    <tr>
        <td>ID</td>
        <td>Day</td>
        <td>Category</td>
        <td>Account</td>
        <td>Amount</td>
        <td>Note</td>
        <td>Actions</td>
    </tr>
    <c:forEach items="${operationDtoList}" var="operationDto">
        <tr>
            <td>${operationDto.id}</td>
            <td>${operationDto.day}</td>
            <td>${operationDto.categoryTitle}</td>
            <td>${operationDto.accountTitle}</td>
            <td>${operationDto.amount}</td>
            <td>${operationDto.note}</td>
            <td>
                <spring:url var="deleteUrl" value="/operation/${operationDto.id}/delete"/>
                <form class="inline-form" method="post" action="${deleteUrl}">
                    <input type="hidden" name="return_first_day" value="${returnFirstDay}">
                    <input type="hidden" name="return_last_day" value="${returnLastDay}">
                    <c:forEach var="returnAccountId" items="${returnAccountIds}">
                        <input type="hidden" name="return_account_id" value="${returnAccountId}">
                    </c:forEach>
                    <button>Delete</button>
                </form>
            </td>
        </tr>
    </c:forEach>
</table>

<jsp:include page="/WEB-INF/pages/footer.jsp"/>
