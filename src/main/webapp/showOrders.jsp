<%--
  Created by IntelliJ IDEA.
  User: zesheng.li
  Date: 4/4/22
  Time: 01:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="header.jsp" %>

<div class="container">
    <h2>${heading}</h2>
    <div class="container">
        <c:if test="${empty orders}">
            <h3> No orders found! </h3>
        </c:if>
        <c:if test="${not empty orders}">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th>Date</th>
                    <th>Symbol</th>
                    <th>Name</th>
                    <th>Order Type</th>
                    <th># Shares</th>
                    <th>filled Price</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${orders}" var="cd">
                    <tr>
                        <td>${cd.getDate()}</td>
                        <td>${cd.getSymbol()}</td>
                        <td>${cd.getShareName()}</td>
                        <td>${cd.getOrderType(cd.getShare())}</td>
                        <td>${cd.getShare()}</td>
                        <td>${cd.getAvgPriceStr()}</td>
                        <td>
                            <c:choose>
                                <c:when test="${cd.getClass().getSimpleName() == 'MarketOrder'}">
                                    <strong>Buy/Sell: </strong>${cd.buySellType}
                                </c:when>
                                <c:when test="${cd.getClass().getSimpleName() == 'MarketOnCloseOrder'}">
                                    <strong>Buy/Sell: </strong>${cd.buySellType}
                                </c:when>
                                <c:when test="${cd.getClass().getSimpleName() == 'TrailingStopOrder'}">
                                    <strong>Percentage: </strong>${cd.percentage}
                                    <form method="post" action="getOrderPriceHistory">
                                        <input type="hidden" name="orderId" value="${cd.id}" />
                                        <input type="submit" value="price history" class="btn btn-info"/>
                                    </form>
                                </c:when>
                                <c:when test="${cd.getClass().getSimpleName() == 'HiddenStopOrder'}">
                                    <strong>PricePerShare: </strong>${cd.pricePerShare}
                                    <form method="post" action="getOrderPriceHistory">
                                        <input type="hidden" name="orderId" value="${cd.id}" />
                                        <input type="submit" value="price history" class="btn btn-info"/>
                                    </form>
                                </c:when>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:if>
    </div>
    <%@ include file="footer.jsp" %>
