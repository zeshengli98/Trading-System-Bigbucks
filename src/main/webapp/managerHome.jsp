<%--
  Created by IntelliJ IDEA.
  User: zesheng.li
  Date: 4/3/22
  Time: 10:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
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
    <h2>Manager Options:</h2>
<%--    <%--%>
<%--        String email = (String) session.getAttribute("email");--%>
<%--        String role = (String) session.getAttribute("role");--%>

<%--        //redirect to appropriate home page if already logged in--%>
<%--        if (email != null) {--%>
<%--            if (role.equals("customerRepresentative")) {--%>
<%--                response.sendRedirect("customerRepresentativeHome.jsp");--%>
<%--            } else if (!role.equals("manager")) {--%>
<%--                response.sendRedirect("home.jsp");--%>
<%--            }--%>
<%--        } else {--%>
<%--            // redirect to log in if not alreaddy logged in--%>
<%--            response.sendRedirect("index.jsp");--%>
<%--        }--%>

<%--    %>--%>

    <%--    <div class="row">--%>
    <%--        <div class="col">--%>
    <div class="card">
        <div class="card-body">
            <h5 class="card-title">Manage Portfolio</h5>
            <div class="container">
                <form action="ManagerViewAllStocks">
                    <button type="submit" class="btn btn-outline-info"><i class="fas fa-paperclip mr-2"></i>View all holdings</button>
                    <%--                            <input type="submit" value="Add Employee" class="btn btn-outline-info"/>--%>
                </form>
                <form action="ManagerViewCurrentOrder" class="pt-1">
                    <button type="submit" class="btn btn-outline-info"><i class="far fa-edit mr-2"></i>View current market orders</button>
                    <%--                            <input type="submit" value="View / Edit / Delete Employee" class="btn btn-outline-info"/>--%>
                </form>
                <form action="ManagerViewOverallRR" class="pt-1">
                    <button type="submit" class="btn btn-outline-info"><i class="fas fa-columns mr-2"></i>View overall risk-return profile</button>
                    <%--                            <input type="submit" value="View sales report" class="btn btn-outline-info"/>--%>
                </form>

            </div>
        </div>
    </div>
    <%--        </div>--%>
    <%--        <div class="col">--%>
    <div class="card">
        <div class="card-body">
            <h5 class="card-title">Sales and orders</h5>
            <div class="container">

                <form action="viewSummaryListing.jsp" class="pt-1">
                    <button type="submit" class="btn btn-outline-info"><i class="far fa-chart-bar mr-2"></i>View Revenue Summary</button>
                    <%--                            <input type="submit" value="View Revenue Summary" class="btn btn-outline-info"/>--%>
                </form>
                <form action="getHighestRevenueEmployee" class="pt-1">
                    <button type="submit" class="btn btn-outline-info"><i class="fas fa-award mr-2"></i>Highest Revenue Customer Representative</button>
                    <%--                            <input type="submit" value="Highest Revenue Customer Representative" class="btn btn-outline-info"/>--%>
                </form>
                <form action="getHighestRevenueCustomer" class="pt-1">
                    <button type="submit" class="btn btn-outline-info"><i class="fas fa-certificate mr-2"></i>Highest Revenue Customer</button>
                    <%--                            <input type="submit" value="Highest Revenue Customer" class="btn btn-outline-info"/>--%>
                </form>
                <form action="viewSearchOrders" class="pt-1">
                    <button type="submit" class="btn btn-outline-info"><i class="fas fa-atom mr-2"></i>Search orders</button>
                    <%--                            <input type="submit" value="Search orders" class="btn btn-outline-info"/>--%>
                </form>
            </div>
        </div>
    </div>
    <%--        </div>--%>
    <%--        <div class="col">--%>
    <div class="card">
        <div class="card-body">
            <h5 class="card-title">Stocks</h5>
            <div class="container">
                <form action="viewSetStockPrice">
                    <button type="submit" class="btn btn-outline-info"><i class="fas fa-calculator mr-2"></i>Set stock price</button>
                    <%--                            <input type="submit" value="Set stock price" class="btn btn-outline-info"/>--%>
                </form>
                <form action="getStocks" class="pt-1">
                    <button type="submit" class="btn btn-outline-info"><i class="fas fa-laptop-code mr-2"></i>View all Stocks</button>
                    <%--                            <input type="submit" value="View all Stocks" class="btn btn-outline-info"/>--%>
                </form>

                <form action="getBestsellers" class="pt-1">
                    <button type="submit" class="btn btn-outline-info"><i class="fas fa-chart-area mr-2"></i>View Bestsellers</button>
                    <%--                            <input type="submit" value="View Bestsellers" class="btn btn-outline-info"/>--%>
                </form>

                <form action="getActivelyTradedStocks" class="pt-1">
                    <button type="submit" class="btn btn-outline-info"><i class="fas fa-chart-line mr-2"></i>View actively traded stocks</button>
                    <%--                            <input type="submit" value="View actively traded stocks" class="btn btn-outline-info"/>--%>
                </form>
            </div>
        </div>
        <%--            </div>--%>
        <%--        </div>--%>

    </div>


</div>
<%@ include file="footer.jsp" %>
