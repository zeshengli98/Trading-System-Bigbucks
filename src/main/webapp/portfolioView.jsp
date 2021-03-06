<%--
  Created by IntelliJ IDEA.
  User: tonghaoyang
  Date: 4/3/22
  Time: 11:10 PM
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
    <pre style="font-family: Arial; text-align: center">${summary}</pre>
    <div class="container">
        <c:if test="${empty holdings}">
        <h3> No stocks found! <h3/>
            </c:if>
            <c:if test="${not empty holdings}">
            <table class="table table-striped">
                <thead>
                <tr>
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
                <c:forEach items="${holdings}" var="stock">
                    <tr>

                        <td>${stock.getSymbol()}</td>
                        <td>${stock.getShareName()}</td>
                        <td>${stock.getShare()}</td>
                        <td>${stock.getAvgPriceStr()}</td>
                        <td>${stock.getTotalCostStr()}</td>
                        <td>${stock.getCurrentPriceStr()}</td>
                        <td>${stock.getCurrentValueStr()}</td>
                        <td>${stock.getProfitStr()}</td>
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
