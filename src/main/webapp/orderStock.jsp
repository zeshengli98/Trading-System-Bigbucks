<%@ page import="com.ibm.security.appscan.bigbucks.util.ServletUtil" %><%--
  Created by IntelliJ IDEA.
  User: zesheng.li
  Date: 4/3/22
  Time: 10:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="header.jsp" %>
<div class="container">
    <h2>Add order</h2>

    <form action="doorder" method="post">
        <div class="form-group">
            <label for="marketType">Market Type</label>
            <select name="marketType" id="marketType" class="form-control">
                <option selected>MarketOnClose</option>
                <option>LiveMarket</option>

            </select>
        </div>

        <div class="form-group">
            <label for="targetSymbol">Stock Symbol:</label>
            <input type="text" class="form-control" id="targetSymbol" name="shareName" required>
        </div>

        <div id='orderNumShares-group' class="form-group">
            <label for="orderNumShares">Number of Shares</label>
            <input type="number" class="form-control" id="orderNumShares" name="orderSize" required>
        </div>

        <div id="orderBuySellType-group" class="form-group">
            <label for="orderType">Buy/Sell</label>
            <select name="orderType" id="orderType" class="form-control">
                <option>Buy</option>
                <option>Sell</option>
            </select>
        </div>

        <div id='date-group' lass="form-group">
            <label for="orderDate">Date(format: yyyy-MM-dd)</label>
            <input type="date" class="form-control" id="orderDate" name="orderDate" pattern="\d{4}-\d{2}-\d{2}">
        </div>
        <br>
        <input type="submit" value="Submit Order" class="btn btn-info">

    </form>

    <p><span id="_ctl0__ctl0_Content_Main_message" style="color:#FF0066;font-size:12pt;font-weight:bold;">
            <%
        java.lang.String error = (String) request.getAttribute("ordermsg");
        System.out.println("jsp:"+error);
        if (error != null && error.trim().length() > 0) {
            request.getSession().removeAttribute("ordermsg");
            out.print(error);
        }

    %>
    <br>


</div>

<script>

    $('#marketType').on('change', function(){
        console.log(this.value);
        if (this.value === "LiveMarket")
        {
            $('#date-group').addClass('d-none');
        }
        else if (this.value === "MarketOnClose")
        {
            $('#date-group').removeClass('d-none');
        }
    })
</script>
<%@ include file="footer.jsp" %>