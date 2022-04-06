<%--
  Created by IntelliJ IDEA.
  User: tonghaoyang
  Date: 4/6/22
  Time: 2:33 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="heading"  value="Search stocks"/>
<%@ include file="header.jsp" %>

<div class="container">
    <h2>Search Options:</h2>
    <div class="row">
        <div class="col">
            <div class="card">
                <div class="card-body">
                    <h5 class="card-title">Search by Symbol</h5>
                    <div class="container">
                        <form method="POST" action="getPlotServlet">
                            <label for="itemName">Symbol:</label>
                            <input type="text" class="form-control" id="itemName" name="symbol" placeholder="Symbol">
                            <input type="submit" value="Search" class="btn btn-info"/>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<%@ include file="footer.jsp" %>
