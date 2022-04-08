package com.ibm.security.appscan.bigbucks.servlet;

import javax.imageio.ImageIO;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import com.ibm.security.appscan.bigbucks.util.PlotUtil;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.data.category.DefaultCategoryDataset;

import static org.jfree.chart.servlet.ServletUtilities.saveChartAsPNG;

public class GetPlotServlet  extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String symbol = request.getParameter("symbol");

        JFreeChart priceChart = PlotUtil.createChart(symbol, 1);
        JFreeChart returnChart = PlotUtil.createReturnChart(symbol, 1);
        String priceChartFileName = null;
        String returnChartFileName = null;
        try {
            priceChartFileName = saveChartAsPNG(priceChart,
                    500,
                    500,
                    null);

            returnChartFileName = saveChartAsPNG(returnChart,
                    500,
                    500,
                    null);

        }
        catch (Exception e){
            e.printStackTrace();
        }

                request.setAttribute("priceChart", priceChartFileName);
                request.setAttribute("returnChart", returnChartFileName);
                request.getRequestDispatcher("searchPlot.jsp" +"?symbol=" + symbol).forward(request, response);
            }

}