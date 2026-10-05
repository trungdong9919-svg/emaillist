<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/header.html" %>

    <h1>Join our email list</h1>
    <p>To join our email list, enter your name and email address below.</p>

    <c:if test="${not empty errorMessage}">
        <p class="error">${errorMessage}</p>
    </c:if>

    <form action="emailList" method="post">
        <input type="hidden" name="action" value="add">

        <label>Email:</label>
        <input type="email" name="email" id="email"
               value="${param.email}" required><br>

        <label>First Name:</label>
        <input type="text" name="firstName" id="firstName"
               value="${param.firstName}" required><br>

        <label>Last Name:</label>
        <input type="text" name="lastName" id="lastName"
               value="${param.lastName}" required><br>

        <label>&nbsp;</label>
        <input type="submit" value="Join Now" id="submit">
    </form>

<%@ include file="/includes/footer.jsp" %>
