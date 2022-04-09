package com.ibm.security.appscan.bigbucks.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import com.ibm.security.appscan.bigbucks.util.DBUtil;
import com.ibm.security.appscan.bigbucks.util.PlotUtil;
import org.jfree.chart.JFreeChart;

import static org.jfree.chart.servlet.ServletUtilities.saveChartAsPNG;

public class GetPlotServlet  extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String symbol = request.getParameter("symbol").trim();
        if (symbol.length() > 0) {
            System.out.println("symbol: " + symbol);
            symbol = symbol.toUpperCase(Locale.ROOT);
            request.setAttribute("symbol", symbol);
            List<String> stocks= null;
            try {
                stocks = Arrays.asList(DBUtil.getStocksInDB());
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
            if(!stocks.contains(symbol)){
                request.setAttribute("errmsg", "Search failed, please check the stock symbol you input.");
                request.getRequestDispatcher("searchPlot.jsp" + "?symbol=" + symbol).forward(request, response);
                return;
            }

            JFreeChart priceChart = PlotUtil.createChart(symbol, 1);
            JFreeChart returnChart = PlotUtil.createReturnChart(symbol, 1);
            JFreeChart autocorrelationChart = PlotUtil.createAutocorrelationChart(symbol, 1);
            JFreeChart histChart = PlotUtil.createHistChart(symbol, 1);
            JFreeChart cumReturnChart = PlotUtil.createCumReturnChart(symbol, 1);
            JFreeChart dailyReturnChart = PlotUtil.createDailyReturnChart(symbol, 1);
            JFreeChart CAPMChart = PlotUtil.createCAPMChart(symbol, 1);

            String priceChartFileName = null;
            String returnChartFileName = null;
            String autoChartFileName = null;
            String histChartFileName = null;
            String cumReturnFileName = null;
            String dailyReturnFileName = null;
            String CAPMFileName = null;
            try {
                priceChartFileName = saveChartAsPNG(priceChart,
                        500,
                        500,
                        null);
                returnChartFileName = saveChartAsPNG(returnChart,
                        500,
                        500,
                        null);
                autoChartFileName = saveChartAsPNG(autocorrelationChart,
                        500,
                        500,
                        null);
                histChartFileName = saveChartAsPNG(histChart,
                        500,
                        500,
                        null);
                cumReturnFileName = saveChartAsPNG(cumReturnChart,
                        500,
                        500,
                        null);
                dailyReturnFileName = saveChartAsPNG(dailyReturnChart,
                        500,
                        500,
                        null);

                CAPMFileName = saveChartAsPNG(CAPMChart,
                        500,
                        500,
                        null);
            } catch (Exception e) {
                e.printStackTrace();
            }

            request.setAttribute("priceChart", priceChartFileName);
            request.setAttribute("returnChart", returnChartFileName);
            request.setAttribute("autoChart", autoChartFileName);
            request.setAttribute("histChart", histChartFileName);
            request.setAttribute("cumReturnChart", cumReturnFileName);
            request.setAttribute("dailyReturnChart", dailyReturnFileName);
            request.setAttribute("CAPMChart", CAPMFileName);
            request.getRequestDispatcher("searchPlot.jsp" + "?symbol=" + symbol).forward(request, response);

        }
        else{
            response.sendRedirect("searchPlot.jsp");
        }
    }


}