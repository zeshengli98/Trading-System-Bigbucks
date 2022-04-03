
package com.ibm.security.appscan.bigbucks.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ibm.security.appscan.Log4Bigbucks;
import com.ibm.security.appscan.bigbucks.util.DBUtil;
import com.ibm.security.appscan.bigbucks.util.ServletUtil;

/**
 * This servlet processes user's login and logout operations
 * Servlet implementation class LoginServlet
 * @author Alexei
 */
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//log in
		// Create session if there isn't one:
		HttpSession session = request.getSession(true);

		String username = null;
		
		try {
			username = request.getParameter("uid");
			if (username != null)
				username = username.trim().toLowerCase();
			
			String password = request.getParameter("passw");
			password = password.trim().toLowerCase(); //in real life the password usually is case sensitive and this cast would not be done
			
			if (!DBUtil.isValidUser(username, password)){
				Log4Bigbucks.getInstance().logError("Login failed >>> User: " +username + " >>> Password: " + password);
				throw new Exception("Login Failed: We're sorry, but this username or password was not found in our system. Please try again.");
			}
		} catch (Exception ex) {
			request.getSession(true).setAttribute("loginError", ex.getLocalizedMessage());
			response.sendRedirect("login.jsp");
			return;
		}

		//Handle the cookie using ServletUtil.establishSession(String)
		try{
			Cookie accountCookie = ServletUtil.establishSession(username,session);
			response.addCookie(accountCookie);
			response.sendRedirect(request.getContextPath()+"/bank/main.jsp");
			}
		catch (Exception ex){
			ex.printStackTrace();
			response.sendError(500);
		}
			
		
		return;
	}

}
