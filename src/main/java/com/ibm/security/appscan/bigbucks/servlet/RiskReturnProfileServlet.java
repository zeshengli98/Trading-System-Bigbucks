package com.ibm.security.appscan.bigbucks.servlet;

import com.ibm.security.appscan.bigbucks.dto.RiskReturnProfileDto;
import com.ibm.security.appscan.bigbucks.util.DBUtil;
import com.ibm.security.appscan.bigbucks.util.ServletUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.ibm.security.appscan.bigbucks.dto.RiskReturnProfileDto.*;
import static com.ibm.security.appscan.bigbucks.util.DBUtil.getAccounts;
import static com.ibm.security.appscan.bigbucks.util.DBUtil.getStocksInDB;

@WebServlet(name = "RiskReturnProfileServlet", value = "/RiskReturnProfileServlet")
public class RiskReturnProfileServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        response.sendRedirect("viewRiskReturnProfile.jsp");
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
            List<RiskReturnProfileDto>profile =  getRiskRetHistory(accountId);
            ArrayList<RiskReturnProfileDto> profile2 = getRiskRetHistory(accountId);
            double ret = calAvgRet(profile2);
            double std = calStd(profile2);
            double sharpe = calSharpeRatio(profile2);
            String summary = "Portfolio Average Return: " + String.format("%.2f", ret*100) + "%\t\t\t"
                    + "Portfolio Volatility: " + String.format("%.2f", std*100) + "%\t\t\t"
                    + "Sharpe Ratio: " + String.format("%.4f", sharpe);
            request.getSession(true).setAttribute("summary", summary);
            request.setAttribute("profile", profile);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        request.setAttribute("heading", "Risk-Return Profile");
        RequestDispatcher rd = request.getRequestDispatcher("viewRiskReturnProfile.jsp");
        rd.forward(request, response);
    }
}
