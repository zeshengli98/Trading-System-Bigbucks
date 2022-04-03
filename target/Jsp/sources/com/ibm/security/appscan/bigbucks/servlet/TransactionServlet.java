package com.ibm.security.appscan.bigbucks.servlet;

import com.ibm.security.appscan.bigbucks.util.DBUtil;
import com.ibm.security.appscan.bigbucks.util.OperationsUtil;
import com.ibm.security.appscan.bigbucks.util.ServletUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class TransactionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req, resp);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        if(!ServletUtil.isLoggedin(request)){
            response.sendRedirect("login.jsp");
            return ;
        }
        System.out.println(request.getParameter("orderSize"));
        int orderSize = Integer.parseInt(request.getParameter("orderSize"));
        String symbol = request.getParameter("shareName");
        String orderType = request.getParameter("orderType");
        System.out.println(orderType);
        System.out.println(orderSize);
        long debitActId = 0;
        String username = ServletUtil.getUser(request).getUsername();
        System.out.println(username);
        try {
            debitActId = DBUtil.getAccounts(username)[0].getAccountId();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        double price = DBUtil.getMarketPrice(symbol).doubleValue();
        System.out.println(debitActId);
        try {
            String orderError = DBUtil.orderStocks(username, debitActId, orderType, symbol, orderSize, price);
        } catch (SQLException e) {
                e.printStackTrace();
                response.sendRedirect("fail.jsp");
        }


        response.sendRedirect("order_submitted.jsp");
    }
}
