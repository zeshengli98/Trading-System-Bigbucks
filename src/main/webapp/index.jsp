<%--
  Created by IntelliJ IDEA.
  User: zesheng.li
  Date: 3/31/22
  Time: 20:36
  To change this template use File | Settings | File Templates.
  //fix the taglib error: https://stackoverflow.com/questions/31043869/intellij-and-jsp-jstl-cannot-resolve-taglib-for-jstl-in-tomcat7
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="heading" value="Home"/>
<%@ include file="header.jsp" %>

<style type="text/css">
    body {
        background-color:#F0F8FF;
    }
    .logo {
        display: block;
        margin-left: auto;
        margin-right: auto;
    }
    .container{
        width: 30%;
    }
    h5{
        color: #1B4965;
    }
</style>
<div class="container" style = "width:30%!important;">
            <img src="Images/giphy.gif" alt="bigbucks" class="logo" style = "width:320px;height:280px;">
            <h5>Login</h5>
            <br>
        <p><span id="_ctl0__ctl0_Content_Main_message" style="color:#FF0066;font-size:12pt;font-weight:bold;">
            <%
                java.lang.String error = (String) request.getSession(true).getAttribute("loginError");

                if (error != null && error.trim().length() > 0) {
                    request.getSession().removeAttribute("loginError");
                    out.print(error);
                }

            %>
            <form action="dologin", method="post">
                <div class="form-group">
                    <input type="text" class="form-control" name="username" placeholder="username" style="width:350px;">
                </div>
                <div class="form-group">
                    <input type="password" class="form-control" name="password" placeholder="password" style="width:350px;">
                </div>
                <div class="form-group">
                    <select class="form-control" name="role" style="width:350px;">
                        <option value="customer">Customer</option>
                        <option value="manager">Manager</option>
                    </select>
                </div>
                <input type="submit" value="Login" class="btn btn-info"/>

                <a style="padding-left: 65px" href="signup.jsp">Signup</a>
            </form>
        </div>


<%@ include file="footer.jsp" %>