package com.ibm.security.appscan.bigbucks.servlet;

import com.ibm.security.appscan.Log4Bigbucks;
import com.ibm.security.appscan.bigbucks.model.Account;
import com.ibm.security.appscan.bigbucks.model.Portfolio;
import com.ibm.security.appscan.bigbucks.util.DBUtil;
import com.ibm.security.appscan.bigbucks.util.ServletUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class ShowPortfolioServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(!ServletUtil.isLoggedin(request)){
            response.sendRedirect("index.jsp");
        }
        else{
            String username = ServletUtil.getUser(request).getUsername();
            long accountId = 0;
            try {
                accountId = DBUtil.getAccounts(username)[0].getAccountId();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            ArrayList<Portfolio> holdings = DBUtil.getPortfoliosByAccount(accountId);
            request.setAttribute("holdings", holdings);
            System.out.println("size: " + holdings.size());
            double cash = -1;
            try {
                cash = Account.getAccount(accountId).getBalance();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            double marketPrice = 0;
            double profit = 0;
            for(Portfolio p:holdings){
                marketPrice += p.getCurrentValue();
                profit += p.getProfit();
            }
            String summary = "Cash: " + String.format("%.2f", cash) + "\t\t\t"
                    + "Market Price: " + String.format("%.2f", marketPrice) + "\t\t\t"
                    + "Asset: " + String.format("%.2f", marketPrice + cash) + "\t\t\t"
                    + "Total Profit:" +  String.format("%.2f",profit);


            request.setAttribute("heading", "Current stock holdings");
            request.setAttribute("summary", summary);
            RequestDispatcher rd = request.getRequestDispatcher("portfolioView.jsp");
            rd.forward(request, response);
        }
    }
}
