package com.ibm.security.appscan.bigbucks.servlet;

import com.ibm.security.appscan.bigbucks.model.Portfolio;
import com.ibm.security.appscan.bigbucks.util.ServletUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;

import static com.ibm.security.appscan.bigbucks.util.DBUtil.getAllUsersPortfolios;

@WebServlet(name = "ManagerViewAllStocksServlet", value = "/ManagerViewAllStocksServlet")
public class ManagerViewAllStocksServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(!ServletUtil.isLoggedin(request)){
            response.sendRedirect("index.jsp");
        }
        ArrayList<Portfolio> portfolios = getAllUsersPortfolios();
        request.setAttribute("portfolios", portfolios);

        request.setAttribute("heading", "All Users' Stocks");
        RequestDispatcher rd = request.getRequestDispatcher("ManagerViewAllStocks.jsp");
        rd.forward(request, response);


    }
}
