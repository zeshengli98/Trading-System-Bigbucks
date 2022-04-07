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

        JFreeChart chart = PlotUtil.createChart(symbol, 1);
        String fileName = null;
        try {
            fileName = saveChartAsPNG(chart,
                    500,
                    500,
                    null);
        }
        catch (Exception e){
            e.printStackTrace();
        }
                System.out.println("name: " + fileName);
                System.out.println(ServletUtilities.getTempFilePrefix());
                request.setAttribute("fileName", fileName);
                request.getRequestDispatcher("searchPlot.jsp" +"?symbol=" + symbol).forward(request, response);
            }

}