<%--
  Created by IntelliJ IDEA.
  User: zesheng.li
  Date: 4/3/22
  Time: 01:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="heading" value="Home"/>
<jsp:include page="header.jsp"/>

<div id="wrapper" style="width: 99%;">
    <td valign="top" colspan="3" class="bb">
        <div class="fl" style="width: 99%;">

            <h1>Signup for BigBucks</h1>

            <!-- To get the latest admin login, please contact SiteOps at 415-555-6159 -->
            <p><span id="_ctl0__ctl0_Content_Main_message" style="color:#ff0066;font-size:12pt;font-weight:bold;">
		<%
            java.lang.String error = (String) request.getSession().getAttribute("message");

            if (error != null && error.trim().length() > 0) {
                request.getSession().removeAttribute("message");
                out.print(error);
            }
        %>
		</span></p>
            <h5>New User Sign up</h5>

            <form action="dosignup" method="post">
                <table>
                    <tr>
                        <td>
                            Firstname:
                        </td>
                        <td>
                            <input type="text" id="name1" name="name1" style="width: 150px;">
                        </td>
                        <td>
                        </td>

                    </tr>
                    <tr>
                        <td>
                            Lastname:
                        </td>
                        <td>
                            <input type="text" id="name2" name="name2" style="width: 150px;">
                        </td>
                        <td>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Username:
                        </td>
                        <td>
                            <input type="text" id="username" name="username" value="" style="width: 150px;">
                        </td>
                        <td>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Password:
                        </td>
                        <td>
                            <input type="password" id="pass1" name="pass1" style="width: 150px;">
                        </td>
                        <td>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            Confirmpassword:
                        </td>
                        <td>
                            <input type="password" id="pass2" name="pass2" style="width: 150px;">
                        </td>
                    </tr>
                    <tr>
                        <td></td>
                        <td>
                            <input type="submit" name="signup" value="Create">
                        </td>
                    </tr>
                </table>
            </form>



        </div>

        <script type="text/javascript">
            function setfocus() {
                if (document.signup.name1.value == "") {
                    document.signup.name1.focus();
                } else if (document.signup.name2.value == "") {
                    document.signup.name2.focus();
                } else if (document.signup.username.value == "") {
                    document.signup.username.focus();
                } else if (document.signup.pass1.value == "") {
                    document.signup.pass1.focus();
                } else if (document.signup.pass2.value == "") {
                    document.signup.pass2.focus();
                }
            }
        </script>


    </td>
</div>

<jsp:include page="footer.jsp"/>
