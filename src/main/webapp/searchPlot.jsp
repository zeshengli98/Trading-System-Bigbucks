<%--
  Created by IntelliJ IDEA.
  User: tonghaoyang
  Date: 4/6/22
  Time: 2:33 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="java.io.*, javax.imageio.*, java.awt.image.*, javax.servlet.*, javax.servlet.http.*,org.jfree.chart.*, org.jfree.chart.plot.*, org.jfree.data.category.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="heading"  value="Search stocks"/>
<body>
<%@ include file="header.jsp" %>
<%
    String symbol = request.getParameter("symbol");
    String value = "";
    if(symbol != null){
        value = symbol;
    }
%>
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
                            <input type="text" class="form-control" id="itemName" name="symbol" value=<%=value%>>
                            <input type="submit" value="Search" class="btn btn-info"/>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<%
    /* Set the HTTP Response Type */
    String prefix = request.getContextPath() +"/servlet/DisplayChart?filename=";
    String priceChartURL = prefix + (String) request.getAttribute("priceChart");
    String returnChartURL = prefix + (String) request.getAttribute("returnChart");
    String autoChartURL = prefix + (String) request.getAttribute("autoChart");
    String histChartURL = prefix + (String) request.getAttribute("histChart");
%>

<img src="<%=priceChartURL%>" border="0" usemap="#<%=priceChartURL%>"/>

<img src="<%=returnChartURL%>" border="0" usemap="#<%=returnChartURL%>"/>

<img src="<%=autoChartURL%>" border="0" usemap="#<%=autoChartURL%>"/>

<img src="<%=histChartURL%>" border="0" usemap="#<%=autoChartURL%>"/>

<%@ include file="footer.jsp" %>
</body>



