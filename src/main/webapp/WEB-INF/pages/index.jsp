<%@page language="java" contentType="text/html;charset=utf-8" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<jsp:include page="/WEB-INF/pages/header.jsp"/>

<spring:url value="/summary" var="summaryUrl"/>
<form method="get" action="${summaryUrl}">
    <table class="form">
        <tr>
            <td><label for="first_day">First day</label></td>
            <td><input type="text" name="first_day" id="first_day" value="${firstDay}"></td>
        </tr>
        <tr>
            <td><label for="last_day">Last day</label></td>
            <td><input type="text" name="last_day" id="last_day" value="${lastDay}"></td>
        </tr>
        <c:forEach var="accountDto" items="${accountDtoList}">
            <tr>
                <td></td>
                <td>
                    <label>
                        <input type="checkbox" name="account_id" id="account_id_${accountDto.id}"
                               value="${accountDto.id}" checked> ${accountDto.title}
                    </label>
                </td>
            </tr>
        </c:forEach>
        <tr>
            <td></td>
            <td>
                <input type="submit" value="Submit">
            </td>
        </tr>
    </table>
</form>

<jsp:include page="/WEB-INF/pages/footer.jsp"/>
