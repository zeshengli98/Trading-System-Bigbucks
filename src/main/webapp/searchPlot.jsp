<%--
  Created by IntelliJ IDEA.
  User: tonghaoyang
  Date: 4/6/22
  Time: 2:33 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="java.io.*, javax.imageio.*, java.awt.image.*, javax.servlet.*, javax.servlet.http.*,org.jfree.chart.*, org.jfree.chart.plot.*, org.jfree.data.category.*"%>
<%@ page import="java.util.Arrays" %>
<%@ page import="com.ibm.security.appscan.bigbucks.util.DBUtil" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.util.List" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="heading"  value="Search stocks"/>
<body>
<%@ include file="header.jsp" %>
<%
    String symbol = (String) request.getAttribute("symbol");
    String errmsg = (String) request.getAttribute("errmsg");
//    List<String> stocks= null;
//    try {
//        stocks = Arrays.asList(DBUtil.getStocksInDB());
//    } catch (SQLException e) {
//        e.printStackTrace();
//    }
//    for(String s:stocks){
//        System.out.println(s);
//    }
//    request.setAttribute("stocks", stocks);
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
                            <c:choose>
                                <c:when test="${symbol == null}">
                                     <input type="text" class="form-control" id="itemName" name="symbol">
                                </c:when>
                                <c:otherwise>
                                    <input type="text" class="form-control" id="itemName" name="symbol" value="<%=symbol%>">
                                </c:otherwise>
                            </c:choose>
<%--                            <select class="form-control" name="symbol">--%>
<%--                                <c:forEach items="${stocks}" var="cd">--%>
<%--                                    <option value="${cd}"> <c:out value = "${cd}"/></option>--%>
<%--                                </c:forEach>--%>
<%--                            </select>--%>
                            <br>
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
    String cumReturnChartURL = prefix + (String) request.getAttribute("cumReturnChart");
    String dailyReturnChartURL = prefix + (String) request.getAttribute("dailyReturnChart");
    String CAPMChartURL = prefix + (String) request.getAttribute("CAPMChart");
%>
<c:if test="${errmsg != null}">
    <p>
        <span id="_ctl0__ctl0_Content_Main_message" style="color:#FF0066;font-size:12pt;font-weight:bold;">
        <%=errmsg%>
        </span>
    </p>
</c:if>


<c:if test="${symbol != null && errmsg == null}">
    <br>
<img src="<%=priceChartURL%>" border="0" usemap="#<%=priceChartURL%>"/>
<img src="<%=returnChartURL%>" border="0" usemap="#<%=returnChartURL%>"/>
    <br>
<img src="<%=autoChartURL%>" border="0" usemap="#<%=autoChartURL%>"/>
<img src="<%=histChartURL%>" border="0" usemap="#<%=autoChartURL%>"/>
    <br>
<img src="<%=cumReturnChartURL%>" border="0" usemap="#<%=autoChartURL%>"/>
<img src="<%=dailyReturnChartURL%>" border="0" usemap="#<%=autoChartURL%>"/>
    <br>
<img src="<%=CAPMChartURL%>" border="0" usemap="#<%=autoChartURL%>"/>
</c:if>

<%@ include file="footer.jsp" %>
</body>



