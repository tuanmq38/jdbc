<%@ page import="com.quintus.jdbc.demo.Student" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: Quintus Mai
  Date: 24/11/2020
  Time: 16:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Student Tracker App</title>
    <link type="text/css" rel="stylesheet" href="css/style.css">
</head>
<body>

<div id="wrapper">
    <div id="header">
        <h2>Quintus University</h2>
    </div>

    <input type="button" value="Add Student"
           onclick="window.location.href='add-student-form.jsp'; return false;"
           class="add-student-button"
    />

    <div id="container">
        <div id="content">
            <table>
                <tr>
                    <th>First Name</th>
                    <th>Last Name</th>
                    <th>Email</th>
                    <th>Action</th>
                </tr>
                <c:forEach var="tempStudent" items="${student_list}">

                    <c:url var="tempLink" value="StudentControllerServlet">
                        <c:param name="command" value="LOAD" />
                        <c:param name="studentId" value="${tempStudent.id}"/>
                    </c:url>

                    <c:url var="deleteLink" value="StudentControllerServlet">
                        <c:param name="command" value="DELETE" />
                        <c:param name="studentId" value="${tempStudent.id}"/>
                    </c:url>

                    <tr>
                        <td> ${tempStudent.firstName}</td>
                        <td> ${tempStudent.lastName}</td>
                        <td> ${tempStudent.email}</td>
                        <td>
                            <a href="${tempLink}">Update</a>
                            <a href="${deleteLink}"
                               onclick="if (!(confirm('Are you sure you want to delete this student?'))) return false">
                                Delete</a>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </div>
</div>
</body>
</html>
