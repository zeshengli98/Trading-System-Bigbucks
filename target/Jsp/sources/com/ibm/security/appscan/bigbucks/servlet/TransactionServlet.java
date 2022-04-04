package com.ibm.security.appscan.bigbucks.servlet;

import com.ibm.security.appscan.bigbucks.util.DBUtil;
import com.ibm.security.appscan.bigbucks.util.OperationsUtil;
import com.ibm.security.appscan.bigbucks.util.ServletUtil;
import lombok.SneakyThrows;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import static com.ibm.security.appscan.bigbucks.util.DBUtil.*;

public class TransactionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//        doPost(req, resp);
        response.sendRedirect("orderStock.jsp");
        try {

            List<String> stocks = Arrays.asList(getStocksInDB());
            request.setAttribute("stocks", stocks);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
        /**
         * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
         */

        protected void doPost (HttpServletRequest request, HttpServletResponse response) throws
        ServletException, IOException {


            if (!ServletUtil.isLoggedin(request)) {
                response.sendRedirect("index.jsp");
                return;
            }


            int orderSize = Integer.parseInt(request.getParameter("orderSize"));
            String symbol = request.getParameter("shareName");
            String orderType = request.getParameter("orderType");
            String marketType = request.getParameter("marketType");
            String date = request.getParameter("orderDate");
            long debitActId = 0;
            String username = ServletUtil.getUser(request).getUsername();

            System.out.println(orderType);
            System.out.println(orderSize);
            System.out.println(date);


            String msg = "";
            try {
                debitActId = DBUtil.getAccounts(username)[0].getAccountId();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            double price= -1;
            System.out.println("marketType:"+marketType);
            if (marketType.equals("MarketOnClose")) {
                try {
                    price = DBUtil.getHistricalClosePrice(symbol, date);
                    System.out.println("marketonclose:"+price);
                } catch (SQLException e) {
                    e.printStackTrace();
                }


            }else if(marketType.equals("LiveMarket")){
                price = getMarketPrice(symbol).doubleValue();

                System.out.println("live:"+price);
            }


            if (price == -1) {
                msg = "Order failed, no " + symbol + " in the database or the date is not trading day";
            }else {
                String orderError = "";
                try {
                    if (marketType.equals("MarketOnClose")) {
                        orderError = DBUtil.orderStocks(username, debitActId, orderType, symbol, orderSize, price, Timestamp.valueOf(date+" 00:00:00"));
                        System.out.println("histryerror: "+msg);
                    }else {

                        orderError = DBUtil.orderStocks(username, debitActId, orderType, symbol, orderSize, price);
                        System.out.println("live: "+msg);
                    }
                    if (orderError == null) {
                        msg = "Order filled!" + orderType + " " + orderSize +
                                " shares of " + symbol.toUpperCase() + ", with price: " + price + ", " + date;
                    } else {
                        msg = orderError;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    response.sendRedirect("fail.jsp");
                }
            }
            System.out.println("msginservlet"+msg);
            request.setAttribute("ordermsg", msg);
//            response.sendRedirect("orderStock.jsp");
            request.getRequestDispatcher("orderStock.jsp").forward(request, response);
        }
    }

