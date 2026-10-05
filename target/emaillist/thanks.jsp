<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/header.html" %>

    <h1>Thanks for joining our email list</h1>

    <p>Here is the information that you entered:</p>

    <label>Email:</label>
    <span>${user.email}</span><br>

    <label>First Name:</label>
    <span>${user.firstName}</span><br>

    <label>Last Name:</label>
    <span>${user.lastName}</span><br>

    <c:choose>
        <c:when test="${emailSent == true}">
            <p class="email-success">
                &#10003; A confirmation email has been sent to <strong>${user.email}</strong>.
            </p>
        </c:when>
        <c:when test="${emailSent == false}">
            <p class="email-warn">
                &#9888; Could not send confirmation email — please check your inbox later.
            </p>
        </c:when>
    </c:choose>

    <p>To enter another email address, click on the Back button in your browser
       or the Return button shown below.</p>

    <form action="emailList" method="post">
        <input type="hidden" name="action" value="join">
        <input type="submit" value="Return">
    </form>

<%@ include file="/includes/footer.jsp" %>

