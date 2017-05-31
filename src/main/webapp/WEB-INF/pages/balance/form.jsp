<%@page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="/WEB-INF/pages/header.jsp"/>

<h1>Balance</h1>
<spring:url var="actionUrl" value="/balance/edit"/>
<form:form method="post" action="${actionUrl}" modelAttribute="balanceDto">
    <input type="hidden" name="return_first_day" value="${returnFirstDay}">
    <input type="hidden" name="return_last_day" value="${returnLastDay}">
    <c:forEach var="returnAccountId" items="${returnAccountIds}">
        <input type="hidden" name="return_account_id" value="${returnAccountId}">
    </c:forEach>
    <form:hidden path="day"/>
    <form:hidden path="accountId"/>
    <table class="form">
        <tr>
            <td>Account</td>
            <td>${balanceDto.accountTitle}</td>
        </tr>
        <tr>
            <td>Day</td>
            <td>${balanceDto.day}</td>
        </tr>
        <tr>
            <td><form:label path="amount">Amount</form:label></td>
            <td><form:input path="amount"/></td>
        </tr>
        <tr>
            <td></td>
            <td>
                <form:label path="manual"><form:checkbox path="manual"/>Manual</form:label>
            </td>
        </tr>
        <tr>
            <td></td>
            <td><form:button>Save</form:button></td>
        </tr>
    </table>
</form:form>

<jsp:include page="/WEB-INF/pages/footer.jsp"/>
