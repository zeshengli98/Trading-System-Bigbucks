package com.ibm.security.appscan.bigbucks.util;
import com.ibm.security.appscan.bigbucks.model.HistoricalData;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;

import java.awt.*;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PlotUtil {

    private static XYDataset createDataset(String symbol, int year) {
        TimeSeries ts = new TimeSeries(symbol);
        Date current = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(current);
        c.add(Calendar.YEAR, -1*year);
        Date start = c.getTime();
        ArrayList<HistoricalData> hs = null;
        try {
            hs = DBUtil.getHistoricalDataByRange(symbol, start, current);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for(HistoricalData data: hs){
            double price = data.getClosePrice();
            Day day = new Day(new Date(data.getDate().getTime()));
            ts.add(day, price);
        }
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(ts);
        return dataset;
    }
    public static XYDataset getReturnDataset(String symbol, int year) {
        TimeSeries ts = new TimeSeries(symbol);
        Date current = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(current);
        c.add(Calendar.YEAR, -1*year);
        Date start = c.getTime();
        ArrayList<HistoricalData> hs = null;


        ArrayList<HistoricalData> historicalData = null;
        try {
            historicalData = DBUtil.getHistoricalDataByRange(symbol, start, current);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for(int i = 1; i<historicalData.size(); i++){
            HistoricalData data2 = historicalData.get(i);
            HistoricalData data1 = historicalData.get(i-1);
            double price2 = data2.getClosePrice();
            double price1 = data1.getClosePrice();
            double ror = (price2 - price1) / price1;
            Day day = new Day(new Date(data2.getDate().getTime()));
            ts.add(day, ror);
        }
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(ts);
        return dataset;
    }


    public static JFreeChart createChart(String StockId, int year) {
        XYDataset dataset = createDataset(StockId, year);

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                StockId,  // title
                "Date",             // x-axis label
                "Price",   // y-axis label
                dataset,false,false,false);

        chart.setBackgroundPaint(Color.WHITE);

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);

        XYItemRenderer r = plot.getRenderer();
        if (r instanceof XYLineAndShapeRenderer) {
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
            renderer.setDrawSeriesLineAsPath(true);
        }
        DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setDateFormatOverride(new SimpleDateFormat("MMM-yyyy"));

        return chart;
    }

    public static JFreeChart createReturnChart(String symbol, int year) {
        XYDataset dataset = getReturnDataset(symbol, year);

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                symbol,  // title
                "Date",             // x-axis label
                "Return",   // y-axis label
                dataset,false,false,false);

        chart.setBackgroundPaint(Color.WHITE);

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);

        XYItemRenderer r = plot.getRenderer();
        if (r instanceof XYLineAndShapeRenderer) {
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
            renderer.setDrawSeriesLineAsPath(true);
        }
        DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setDateFormatOverride(new SimpleDateFormat("MMM-yyyy"));

        return chart;
    }





}
