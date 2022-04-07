<%--
  Created by IntelliJ IDEA.
  User: zesheng.li
  Date: 4/6/22
  Time: 15:54
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
        <c:if test="${empty portfolios}">
        <h3> No stocks found! <h3/>
            </c:if>
            <c:if test="${not empty portfolios}">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th>User</th>
                    <th>Symbol</th>
                    <th>Name</th>
                    <th># Shares</th>
                    <th>Holding Price</th>
                    <th>Total Cost</th>
                    <th>Market Price</th>
                    <th>Asset Value</th>
                    <th>Unrealized Profit</th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${portfolios}" var="portfolios">
                    <tr>
                        <td>${portfolios.getUsername()}</td>
                        <td>${portfolios.getSymbol()}</td>
                        <td>${portfolios.getShareName()}</td>
                        <td>${portfolios.getShare()}</td>
                        <td>${portfolios.getAvgPriceStr()}</td>
                        <td>${portfolios.getTotalCostStr()}</td>
                        <td>${portfolios.getCurrentPriceStr()}</td>
                        <td>${portfolios.getCurrentValueStr()}</td>
                        <td>${portfolios.getProfitStr()}</td>
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

