<%--
  Created by IntelliJ IDEA.
  User: zesheng.li
  Date: 4/6/22
  Time: 19:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="header.jsp" %>
<%@ page import="com.ibm.security.appscan.bigbucks.util.DBUtil" %>

<head>
    <title>View Your Portfolio</title>
</head>
<body>

<div class="container">
    <h2>${heading}</h2>
    <br>
    <div class="container">
        <c:if test="${empty currOrder}">
        <h3> No stocks found! <h3/>
            </c:if>
            <c:if test="${not empty currOrder}">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th>User</th>
                    <th>Symbol</th>
                    <th>Name</th>
                    <th>Order Type</th>
                    <th># Shares</th>
                    <th>filled Price</th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${currOrder}" var="currOrder">
                    <tr>
                        <td>${currOrder.getUsername()}</td>
                        <td>${currOrder.getSymbol()}</td>
                        <td>${currOrder.getShareName()}</td>
                        <td>${currOrder.getOrderType(currOrder.getShare())}</td>
                        <td>${currOrder.getShare()}</td>
                        <td>${currOrder.getAvgPriceStr()}</td>
                            <%--                        <td>${stock.getAmount() - stock}</td>--%>
                        <td></td>
                        <td></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            </c:if>
    </div>
    <%@ include file="footer.jsp" %>
</body>


