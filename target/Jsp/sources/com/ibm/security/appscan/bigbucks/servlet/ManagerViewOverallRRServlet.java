package com.ibm.security.appscan.bigbucks.servlet;

import com.ibm.security.appscan.bigbucks.dto.RiskReturnProfileDto;
import com.ibm.security.appscan.bigbucks.model.Portfolio;
import com.ibm.security.appscan.bigbucks.util.DBUtil;
import com.ibm.security.appscan.bigbucks.util.ServletUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.ibm.security.appscan.bigbucks.dto.RiskReturnProfileDto.*;
import static com.ibm.security.appscan.bigbucks.dto.RiskReturnProfileDto.calSharpeRatio;
import static com.ibm.security.appscan.bigbucks.util.DBUtil.getCurrentOrder;

@WebServlet(name = "ManagerViewOverallRRServlet", value = "/ManagerViewOverallRRServlet")
public class ManagerViewOverallRRServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(!ServletUtil.isLoggedin(request)){
            response.sendRedirect("index.jsp");
        }
        try {
            List<RiskReturnProfileDto>overallprofile =  getAllUsersRiskRetHistory();
            ArrayList<RiskReturnProfileDto> overallprofile2 = getAllUsersRiskRetHistory();
            double ret = calAvgRet(overallprofile2)*252;
            double std = calStd(overallprofile2)*Math.sqrt(252);
            double sharpe = calSharpeRatio(overallprofile2);
            String summary = "Portfolio Annualized Average Return: " + String.format("%.2f", ret*100) + "%\t\t\t"
                    + "Portfolio Annualized Volatility: " + String.format("%.2f", std*100) + "%\t\t\t"
                    + "Sharpe Ratio: " + String.format("%.4f", sharpe);
            request.getSession(true).setAttribute("summary", summary);
            request.setAttribute("overallprofile", overallprofile);
            request.setAttribute("heading", "Over all Risk-Return Profile");
            RequestDispatcher rd = request.getRequestDispatcher("ManagerViewOverallRR.jsp");
            rd.forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
