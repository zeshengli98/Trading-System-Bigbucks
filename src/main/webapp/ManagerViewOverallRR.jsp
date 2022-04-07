<%--
  Created by IntelliJ IDEA.
  User: zesheng.li
  Date: 4/6/22
  Time: 23:55
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
    <%--    <pre style="font-family: Arial; text-align: center">${summary}</pre>--%>
    <p><span id="_ctl0__ctl0_Content_Main_message" style="color:#c90404;font-size:14pt;font-weight:bold;text-align: center">
            <%
                java.lang.String profileSummary = (String) request.getSession(true).getAttribute("summary");
                System.out.println(profileSummary);
                out.print(profileSummary);
            %>
    <div class="container">
        <c:if test="${empty overallprofile}">
        <h3> No stocks found! <h3/>
            </c:if>
            <c:if test="${not empty overallprofile}">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th>Date</th>
                    <th>Cash</th>
                    <th>Asset Value</th>
                    <th>Equity</th>
                    <th>Profit(%)</th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${overallprofile}" var="overallprofile">
                    <tr>

                        <td>${overallprofile.date}</td>
                        <td>${String.format("%.2f", overallprofile.cash)}</td>
                        <td>${String.format("%.2f", overallprofile.asset)}</td>
                        <td>${String.format("%.2f", overallprofile.equity)}</td>
                        <td>${String.format("%.2f", overallprofile.pctPnL*100)}%</td>
                            <%--                        <td>${profile.pctPnL}</td>--%>
                            <%--                        <td>${stock.getCurrentValueStr()}</td>--%>
                            <%--                        <td>${stock.getProfitStr()}</td>--%>
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
