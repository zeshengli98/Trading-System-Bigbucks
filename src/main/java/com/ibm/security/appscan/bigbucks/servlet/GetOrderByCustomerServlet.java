package com.ibm.security.appscan.bigbucks.servlet;

import com.ibm.security.appscan.bigbucks.model.Portfolio;
import com.ibm.security.appscan.bigbucks.util.DBUtil;
import com.ibm.security.appscan.bigbucks.util.ServletUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static com.ibm.security.appscan.bigbucks.util.DBUtil.getCurrentOrder;
import static com.ibm.security.appscan.bigbucks.util.DBUtil.getOrdersByAccount;

@WebServlet(name = "GetOrderByCustomerServlet", value = "/GetOrderByCustomerServlet")
public class GetOrderByCustomerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(!ServletUtil.isLoggedin(request)){
            response.sendRedirect("index.jsp");
        }
        String username = ServletUtil.getUser(request).getUsername();
        long accountId =0;
        try {
            accountId = DBUtil.getAccounts(username)[0].getAccountId();
            List<Portfolio> orders = getOrdersByAccount(accountId);
            request.setAttribute("orders", orders);

            RequestDispatcher rd = request.getRequestDispatcher("showOrders.jsp");
            rd.forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
