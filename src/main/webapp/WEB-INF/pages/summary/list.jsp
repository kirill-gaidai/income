<%@page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/WEB-INF/pages/header.jsp"/>

<c:set var="returnAccountIdsParam" value=""/>
<c:forEach var="returnAccountId" items="${returnAccountIds}">
    <c:set var="returnAccountIdsParam" value="${returnAccountIdsParam}&return_account_id=${returnAccountId}"/>
</c:forEach>

<c:set var="returnParam" value="${returnAccountIdsParam}&return_first_day=${returnFirstDay}&return_last_day=${returnLastDay}"/>

<c:set var="accountIdsParam" value=""/>
<c:forEach items="${summaryDto.accountDtoList}" var="accountDto">
    <c:set var="accountIdsParam" value="${accountIdsParam}&account_id=${accountDto.id}"/>
</c:forEach>

<h1>Summary</h1>
<table class="list">
    <tr>
        <td>Date</td>
        <td>Differences</td>
        <c:forEach items="${summaryDto.accountDtoList}" var="accountDto">
            <td>${accountDto.title}</td>
        </c:forEach>
        <td>Sum</td>
        <c:forEach items="${summaryDto.categoryDtoList}" var="categoryDto">
            <td>${categoryDto.title}</td>
        </c:forEach>
        <td>Sum</td>
    </tr>
    <c:forEach items="${summaryDto.summaryDtoRowList}" var="summaryDtoRow">
        <tr>
            <c:set var="day" value="${summaryDtoRow.day}"/>
            <td>${day}</td>
            <td>${summaryDtoRow.difference}</td>

            <c:set var="index" value="0"/>
            <c:forEach items="${summaryDtoRow.balances}" var="balance">
                <td>
                    <c:set var="accountId" value="${summaryDto.accountDtoList[index].id}"/>
                    <c:set var="index" value="${index + 1}"/>
                    <c:set var="editUrlParams" value="day=${day}&account_id=${accountId}"/>

                    <spring:url var="editUrl" value="/balance/edit?${editUrlParams}${returnParam}"/>
                    <a href="${editUrl}">${balance}</a>
                </td>
            </c:forEach>

            <td>${summaryDtoRow.balancesSummary}</td>

            <c:set var="index" value="0"/>
            <c:forEach items="${summaryDtoRow.amounts}" var="amount">
                <td>
                    <c:set var="categoryId" value="${summaryDto.categoryDtoList[index].id}"/>
                    <c:set var="index" value="${index + 1}"/>
                    <c:set var="editUrlParams" value="day=${day}&category_id=${categoryId}${accountIdsParam}"/>

                    <spring:url var="editUrl" value="/operation/list?${editUrlParams}${returnParam}"/>
                    <a href="${editUrl}">${amount}</a>
                </td>
            </c:forEach>

            <td>
                <c:set var="editUrlParams" value="day=${day}${accountIdsParam}"/>

                <spring:url var="editUrl" value="/operation/list?${editUrlParams}${returnParam}"/>
                <a href="${editUrl}">${summaryDtoRow.amountsSummary}</a>
            </td>
        </tr>
    </c:forEach>
</table>

<jsp:include page="/WEB-INF/pages/footer.jsp"/>
