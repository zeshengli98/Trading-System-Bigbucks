package com.ibm.security.appscan.bigbucks.servlet;

import com.ibm.security.appscan.Log4Bigbucks;
import com.ibm.security.appscan.bigbucks.util.DBUtil;
import com.ibm.security.appscan.bigbucks.util.ServletUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class LoginServlet2 extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet2() {
        super();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //log out
        try {
            HttpSession session = request.getSession(false);
            session.removeAttribute(ServletUtil.SESSION_ATTR_USER);
        } catch (Exception e){
            // do nothing
        } finally {
            response.sendRedirect("index.jsp");
        }

    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //log in
        // Create session if there isn't one:
        HttpSession session = request.getSession(true);

        String username = null;

        try {
            username = request.getParameter("username");
            if (username != null)
                username = username.trim().toLowerCase();

            String password = request.getParameter("password");
            password = password.trim().toLowerCase(); //in real life the password usually is case sensitive and this cast would not be done

            if (!DBUtil.isValidUser(username, password)){
                Log4Bigbucks.getInstance().logError("Login failed >>> User: " +username + " >>> Password: " + password);
                throw new Exception("Login Failed: We're sorry, but this username or password was not found in our system. Please try again.");
            }
        } catch (Exception ex) {
            request.getSession(true).setAttribute("loginError", ex.getLocalizedMessage());
            response.sendRedirect("index.jsp");
            return;
        }

        //Handle the cookie using ServletUtil.establishSession(String)
        try{
            String role = request.getParameter("role");
            Cookie accountCookie = ServletUtil.establishSession(username,session);
            response.addCookie(accountCookie);
            if(role.equals("manager")) {
                response.sendRedirect("managerHome.jsp");
            }
            else if(role.equals("customerRepresentative")) {
                response.sendRedirect("customerRepresentativeHome.jsp");
            }
            else {
                response.sendRedirect("welcome.jsp");//modify to home.jsp
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            response.sendError(500);
        }


        return;
    }
}
