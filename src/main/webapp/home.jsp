<%@ page import="com.ibm.security.appscan.bigbucks.util.ServletUtil" %><%--
  Created by IntelliJ IDEA.
  User: zesheng.li
  Date: 4/3/22
  Time: 01:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="heading" value="Home"/>
<%@ include file="header.jsp" %>
<style type="text/css">
  body {
    background-color:#F0F8FF;
  }
  .card{
    margin:10px 0;
  }
</style>

<div class="container">
  <h2>Customer Options:</h2>
    <%
//        String email = (String)session.getAttribute("email");
//        String role = (String)session.getAttribute("role");
//
//        //redirect to appropriate home page if already logged in
//        if(email != null) {
//            if(role.equals("manager")) {
//                response.sendRedirect("managerHome.jsp");
//            }
//            else if(role.equals("customerRepresentative")) {
//                response.sendRedirect("customerRepresentativeHome.jsp");
//            }
//        }
//        else {
//            // redirect to log in if not alreaddy logged in
//            response.sendRedirect("index.jsp");
//        }
          if(!ServletUtil.isLoggedin(request)){
              response.sendRedirect("index.jsp");
}
    %>
      <div class="row">
      <div class="col">
  <div class="card">
    <div class="card-body">
      <h5 class="card-title">Orders</h5>
      <div class="container">
        <form action="doorder",method="get">
          <button type="submit" class="btn btn-outline-info"><i class="fas fa-bullseye mr-2"></i>Place Order</button>
          <%--                    <input type="submit" value="Place Order" class="btn btn-outline-info"/>--%>
        </form>

        <form action="getOrdersByCustomer" class="pt-1">
          <button type="submit" class="btn btn-outline-info"><i class="fas fa-book mr-2"></i>Order History</button>
          <%--                    <input type="submit" value="Order History" class="btn btn-outline-info"/>--%>
        </form>
      </div>
    </div>
  </div>
  <%--    </div>--%>
  <%--    <div class="col">--%>
  <div class="card">
    <div class="card-body">
      <h5 class="card-title">Stocks</h5>
      <div class="container">
        <form action="getPortfoliosByCustomer">
          <button type="submit" class="btn btn-outline-info"><i class="far fa-file-alt mr-2"></i>Current stock holdings</button>
          <%--                    <input type="submit" value="Current stock holdings" class="btn btn-outline-info"/>--%>
        </form>
        <form action="viewStockHistory" class="pt-1">
          <button type="submit" class="btn btn-outline-info"><i class="fas fa-book-open mr-2"></i>Stock price history</button>
          <%--                    <input type="submit" value="Stock price history" class="btn btn-outline-info"/>--%>
        </form>
        <form action="viewRiskReturn" class="pt-1">
          <button type="submit" class="btn btn-outline-info"><i class="fas fa-atom mr-2"></i>View risk-return profile</button>
          <%--                    <input type="submit" value="Search stocks" class="btn btn-outline-info"/>--%>
        </form>
        <form action="getCustomerBestsellers" class="pt-1">
          <button type="submit" class="btn btn-outline-info"><i class="fas fa-chart-area mr-2"></i>View bestseller stocks</button>
          <%--                    <input type="submit" value="View bestseller stocks" class="btn btn-outline-info"/>--%>
        </form>
        <form action="getStockSuggestions" class="pt-1">
          <button type="submit" class="btn btn-outline-info"><i class="fas fa-glasses mr-2"></i>View suggested stocks</button>
          <%--                    <input type="submit" value="View suggested stocks" class="btn btn-outline-info"/>--%>
        </form>
      </div>
    </div>
    <%--        </div>--%>
    <%--    </div>--%>
  </div>
<%@ include file="footer.jsp" %>