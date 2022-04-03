package com.ibm.security.appscan.bigbucks.servlet;

import java.io.IOException;

import com.ibm.security.appscan.Log4Bigbucks;
import com.ibm.security.appscan.bigbucks.util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class SignUpServlet extends HttpServlet {

    public SignUpServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String username;
        String message;

        try {
            username = request.getParameter("username");
            if (username != null) {
                username = username.trim().toLowerCase();
            }
            String firstname = request.getParameter("name1");
            String lastname = request.getParameter("name2");
            String password1 = request.getParameter("pass1");
            String password2 = request.getParameter("pass2");

            if (username == null || username.trim().length() == 0
                    || password1 == null || password1.trim().length() == 0
                    || password2 == null || password2.trim().length() == 0)
                message = "Username or password can not be empty.";

            else if (!password1.equals(password2)) {
                message = "Two passwords did not match";
            } else if (DBUtil.isExistedUser(username)) {
                message = "Username has already been used, please use another username.";
                request.getSession().setAttribute("message", message);
                Log4Bigbucks.getInstance().logError("Login in failed >>> User: " + username + " >>> Password: " + password1);
                throw new Exception();
            } else {
                message = DBUtil.createAccount(username, password1, firstname, lastname);
            }
            if (message == null)
                message = "Requested operation has completed successfully.";
            request.getSession().setAttribute("message", message);
        } catch (Exception ex) {
            request.getSession(true).setAttribute("loginError", ex.getLocalizedMessage());
        }

        response.sendRedirect("signup.jsp");
    }

}
