package com.ibm.security.appscan.bigbucks.servlet;

import com.ibm.security.appscan.bigbucks.dto.StockDto;
import com.ibm.security.appscan.bigbucks.model.Stock;
import com.ibm.security.appscan.bigbucks.util.DBUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.ibm.security.appscan.bigbucks.dto.StockDto.getHistoryStockDto;
import static com.ibm.security.appscan.bigbucks.util.DBUtil.getConnection;
import static com.ibm.security.appscan.bigbucks.util.DBUtil.getStock;

@WebServlet(name = "viewStockHistoryServlet", value = "/viewStockHistoryServlet")
public class viewStockHistoryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        response.sendRedirect("viewStockHistory.jsp");
        try {
            List<String> stocks= Arrays.asList(DBUtil.getStocksInDB());
            request.setAttribute("stocks", stocks);
            RequestDispatcher rd = request.getRequestDispatcher("viewStockHistory.jsp");
            rd.forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String stockSymbol = request.getParameter("stockSymbol");

        List<StockDto> stocks = null;
        try {
            stocks = getHistoryStockDto(stockSymbol);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        request.setAttribute("stocks", stocks);
        request.setAttribute("heading", "Stock price history");

        RequestDispatcher rd = request.getRequestDispatcher("showStockHistory.jsp");
        rd.forward(request, response);
    }
}
