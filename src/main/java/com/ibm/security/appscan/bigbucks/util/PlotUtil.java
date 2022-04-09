package com.ibm.security.appscan.bigbucks.util;
import com.ibm.security.appscan.bigbucks.model.HistoricalData;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleInsets;
import org.jfree.util.ShapeUtilities;

import javax.sound.midi.SysexMessage;
import java.awt.*;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class PlotUtil {

    private static XYDataset createDataset(String symbol, int year) {
        TimeSeries ts = new TimeSeries(symbol);
        Date current = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(current);
        c.add(Calendar.YEAR, -1 * year);
        Date start = c.getTime();
        ArrayList<HistoricalData> hs = null;
        try {
            hs = DBUtil.getHistoricalDataByRange(symbol, start, current);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (HistoricalData data : hs) {
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
        Calendar c = Calendar.getInstance();
        c.setTime(current);
        c.add(Calendar.YEAR, -1 * year);
        Date start = c.getTime();
        ArrayList<HistoricalData> hs = null;


        ArrayList<HistoricalData> historicalData = null;
        try {
            historicalData = DBUtil.getHistoricalDataByRange(symbol, start, current);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (int i = 1; i < Objects.requireNonNull(historicalData).size(); i++) {
            HistoricalData data2 = historicalData.get(i);
            HistoricalData data1 = historicalData.get(i - 1);
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
                dataset, false, false, false);

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
                dataset, false, false, false);

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

    public static XYDataset getAutocorrelationDataset(String symbol, int year) {
        XYSeries s = new XYSeries(symbol);
        Date current = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(current);
        c.add(Calendar.YEAR, -1 * year);
        Date start = c.getTime();
        ArrayList<HistoricalData> hs = null;

        ArrayList<HistoricalData> historicalData = null;
        try {
            historicalData = DBUtil.getHistoricalDataByRange(symbol, start, current);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Double preRor = null;
        for (int i = 1; i < historicalData.size(); i++) {
            HistoricalData data2 = historicalData.get(i);
            HistoricalData data1 = historicalData.get(i - 1);
            double price2 = data2.getClosePrice();
            double price1 = data1.getClosePrice();
            Double ror = (price2 - price1) / price1;
            if (preRor != null) {
                s.add(preRor, ror);
            }
            preRor = ror;
        }
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(s);

        return dataset;
    }

    public static JFreeChart createAutocorrelationChart(String symbol, int year) {
        XYDataset dataset = getAutocorrelationDataset(symbol, year);

        JFreeChart chart = ChartFactory.createScatterPlot(
                "Two Consecutive Trading Days' Return of " + symbol,  // title
                "Return (-1)",             // x-axis label
                "Return",   // y-axis label
                dataset, PlotOrientation.VERTICAL, false, false, false);

        chart.setBackgroundPaint(Color.WHITE);

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        //plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);
        plot.setDomainCrosshairPaint(Color.BLACK);
        plot.setRangeCrosshairPaint(Color.BLACK);
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.GRAY);
        plot.setRangeGridlinePaint(Color.GRAY);

        plot.setDomainGridlineStroke(new BasicStroke());
        plot.setRangeGridlineStroke(new BasicStroke());

        XYItemRenderer r = plot.getRenderer();
        if (r instanceof XYLineAndShapeRenderer) {
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;

            renderer.setDrawSeriesLineAsPath(true);
            renderer.setUseFillPaint(true);
            renderer.setUseOutlinePaint(true);
            renderer.setSeriesOutlinePaint(0, Color.gray);
            renderer.setBasePaint(Color.gray);
            renderer.setSeriesFillPaint(0, Color.gray);
            renderer.setSeriesShape(0, ShapeUtilities.createDiamond(2.0f));
        }


        NumberAxis xAxis = (NumberAxis) plot.getDomainAxis();
        xAxis.setNumberFormatOverride(NumberFormat.getPercentInstance());

        NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
        yAxis.setNumberFormatOverride(NumberFormat.getPercentInstance());

        return chart;
    }

    private static HistogramDataset createHistDataset(String symbol, int year) {
        ArrayList<HistoricalData> historicalData = null;
        ArrayList<Double> rorData = new ArrayList<Double>();
        Date current = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(current);
        c.add(Calendar.YEAR, -1 * year);
        Date start = c.getTime();
        try {
            historicalData = DBUtil.getHistoricalDataByRange(symbol, start, current);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (int i = 1; i < Objects.requireNonNull(historicalData).size(); i++) {
            HistoricalData data2 = historicalData.get(i);
            HistoricalData data1 = historicalData.get(i - 1);
            double price2 = data2.getClosePrice();
            double price1 = data1.getClosePrice();
            double ror = (price2 - price1) / price1;
            rorData.add(ror);
        }
        HistogramDataset dataset = new HistogramDataset();

        dataset.addSeries(symbol,
                rorData.stream().mapToDouble(Double::doubleValue).toArray(),
                25);
        return dataset;
    }

    public static JFreeChart createHistChart(String symbol, int year) {
        HistogramDataset dataset = createHistDataset(symbol, year);

        JFreeChart chart = ChartFactory.createHistogram(
                "Histogram of " + symbol + " simple return",  // title
                "Return bin",             // x-axis label
                "Frequency",   // y-axis label
                dataset, PlotOrientation.VERTICAL, false, false, false);

        chart.setBackgroundPaint(Color.WHITE);

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(false);
        plot.setDomainCrosshairPaint(Color.BLACK);
        plot.setRangeCrosshairPaint(Color.BLACK);
        plot.setRangeGridlineStroke(new BasicStroke());

        XYItemRenderer r = plot.getRenderer();
        if (r instanceof XYLineAndShapeRenderer) {
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
            renderer.setDrawSeriesLineAsPath(true);
            renderer.setUseFillPaint(true);
            renderer.setUseOutlinePaint(true);
            renderer.setSeriesOutlinePaint(0, Color.gray);
            renderer.setBasePaint(Color.gray);
            renderer.setSeriesFillPaint(0, Color.gray);
            renderer.setSeriesShape(0, ShapeUtilities.createDiamond(2.0f));
        }

        NumberAxis xAxis = (NumberAxis) plot.getDomainAxis();
        xAxis.setNumberFormatOverride(NumberFormat.getPercentInstance());

        return chart;

    }

    private static XYDataset getCumReturnDataset(String symbol, int year) {
        Date current = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(current);
        c.add(Calendar.YEAR, -1 * year);
        Date start = c.getTime();
        ArrayList<HistoricalData> stockData = null;
        ArrayList<HistoricalData> indexData = null;
        try {
            stockData = DBUtil.getHistoricalDataByRange(symbol, start, current);
            indexData = DBUtil.getHistoricalDataByRange("SPY", start, current);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        TimeSeries s1 = new TimeSeries(symbol);
        TimeSeries s2 = new TimeSeries("S&P 500");
        double cumStockReturn = 1;
        double cumIndexReturn = 1;
        double originalStockPrice = Objects.requireNonNull(stockData).get(0).getClosePrice();
        double originalIndexPrice = Objects.requireNonNull(indexData).get(0).getClosePrice();

        for(int i = 1; i<stockData.size(); i++){

            HistoricalData data = stockData.get(i);
            cumStockReturn = data.getClosePrice()/originalStockPrice;
            Day day = new Day(new Date(data.getDate().getTime()));
            s1.add(day, cumStockReturn);
        }

        for(int i = 1; i<indexData.size(); i++){

                HistoricalData data = indexData.get(i);
                cumIndexReturn = data.getClosePrice()/originalIndexPrice;
                Day day = new Day(new Date(data.getDate().getTime()));
                s2.add(day, cumIndexReturn);
            }

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(s1);
        dataset.addSeries(s2);

        return dataset;
    }

    public static JFreeChart createCumReturnChart(String symbol, int year){
        XYDataset dataset = getCumReturnDataset(symbol, year);

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                symbol+ " and S&P 500 cumulative returns",  // title
                "Date",             // x-axis label
                "Relative price",   // y-axis label
                dataset,true,false,false);

        chart.setBackgroundPaint(Color.WHITE);

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        plot.setDomainCrosshairVisible(false);
        plot.setRangeCrosshairVisible(true);
        plot.setRangeCrosshairPaint(Color.BLACK);
        plot.setOutlineStroke(new BasicStroke());

        XYItemRenderer r = plot.getRenderer();
        if (r instanceof XYLineAndShapeRenderer) {
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;

            renderer.setDrawSeriesLineAsPath(true);
        }

        DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setDateFormatOverride(new SimpleDateFormat("MMM-yyyy"));

        return chart;
    }


    private static XYDataset getDailyReturnDataset(String symbol, int year) {
        Date current = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(current);
        c.add(Calendar.YEAR, -1 * year);
        Date start = c.getTime();
        ArrayList<HistoricalData> stockData = null;
        ArrayList<HistoricalData> indexData = null;
        try {
            stockData = DBUtil.getHistoricalDataByRange(symbol, start, current);
            indexData = DBUtil.getHistoricalDataByRange("SPY", start, current);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        TimeSeries s1 = new TimeSeries(symbol);
        TimeSeries s2 = new TimeSeries("S&P 500");
        double stockReturn = 1;
        double indexReturn = 1;
        double preStockPrice = Objects.requireNonNull(stockData).get(0).getClosePrice();
        double preIndexPrice = Objects.requireNonNull(indexData).get(0).getClosePrice();

        for(int i = 1; i<stockData.size(); i++){
            HistoricalData data = stockData.get(i);
            stockReturn = data.getClosePrice()/preStockPrice - 1;
            Day day = new Day(new Date(data.getDate().getTime()));
            s1.add(day, stockReturn);
            preStockPrice = data.getClosePrice();
        }

        for(int i = 1; i<indexData.size(); i++){
            HistoricalData data = indexData.get(i);
            indexReturn = data.getClosePrice()/preIndexPrice - 1;
            Day day = new Day(new Date(data.getDate().getTime()));
            s2.add(day, indexReturn);
            preIndexPrice = data.getClosePrice();
        }

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(s1);
        dataset.addSeries(s2);
        return dataset;
    }

    public static JFreeChart createDailyReturnChart(String symbol, int year){
        XYDataset dataset = getDailyReturnDataset(symbol, year);

        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                "Daily return change for "+ symbol+ " and S&P 500",  // title
                "Date",             // x-axis label
                "Daily returns (%)",   // y-axis label
                dataset,true,false,false);

        chart.setBackgroundPaint(Color.WHITE);

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        plot.setDomainCrosshairVisible(false);
        plot.setRangeCrosshairVisible(true);
        plot.setRangeCrosshairStroke(new BasicStroke());
        plot.setRangeCrosshairPaint(Color.BLACK);
        plot.setOutlineStroke(new BasicStroke());

        XYItemRenderer r = plot.getRenderer();
        if (r instanceof XYLineAndShapeRenderer) {
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
            renderer.setDrawSeriesLineAsPath(true);
        }

        DateAxis axis = (DateAxis) plot.getDomainAxis();
        axis.setDateFormatOverride(new SimpleDateFormat("MMM-yyyy"));

        NumberAxis xAxis = (NumberAxis) plot.getRangeAxis();
        xAxis.setNumberFormatOverride(NumberFormat.getPercentInstance());

        return chart;
    }

    private static double _calBeta(ArrayList<Double> xVariable, ArrayList<Double> yVariable){
        double xMean = xVariable.stream().mapToDouble(d -> d).average().orElse(0.0);
        double yMean = yVariable.stream().mapToDouble(d -> d).average().orElse(0.0);
        double nominator = 0;
        double denominator = 0;
        for (int i=0; i<xVariable.size(); i++){
            nominator += (xVariable.get(i) - xMean) * (yVariable.get(i) - yMean);
            denominator += (xVariable.get(i) - xMean) * (xVariable.get(i) - xMean);
        }
        return nominator / denominator;
    }

    private static double _calAlpha(ArrayList<Double> xVariable, ArrayList<Double> yVariable,
                                    double beta){
        double xMean = xVariable.stream().mapToDouble(d -> d).average().orElse(0.0);
        double yMean = yVariable.stream().mapToDouble(d -> d).average().orElse(0.0);
        return yMean - (beta * xMean);
    }

    private static ArrayList<Double> _findFittedValues(ArrayList<Double> xVariable,
                                                       ArrayList<Double> yVariable){
        double beta = _calBeta(xVariable, yVariable);
        double alpha = _calAlpha(xVariable, yVariable, beta);
        ArrayList<Double> fitted = new ArrayList<>();
        for (Double x : xVariable){
            fitted.add(x * beta + alpha);
        }
        return fitted;
    }

    private static ArrayList<Double> _stringListToDouble(ArrayList<String> list){
        ArrayList<Double> result = new ArrayList<>();
        for (String s : list) {
            double temp = 0;
            try {
                temp = Double.parseDouble(s);
            }
            catch (Exception e) {
                temp=0;
            }
            result.add(temp);
        }
        return result;
    }

    public static JFreeChart createCAPMChart(String symbol, int year) {
        XYDataset dataset = getCAPMDataset(symbol, year);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "CAPM for "+ symbol+ " and S&P 500",  // title
                "S&P 500",             // x-axis label
                symbol,   // y-axis label
                dataset,
                PlotOrientation.VERTICAL,true,true,false);

        chart.setBackgroundPaint(Color.WHITE);

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.WHITE);
        plot.setDomainCrosshairVisible(false);
        plot.setRangeCrosshairVisible(false);
        plot.setOutlineStroke(new BasicStroke());

        XYItemRenderer r = plot.getRenderer();
        if (r instanceof XYLineAndShapeRenderer) {
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
            renderer.setDrawSeriesLineAsPath(true);
            renderer.setUseFillPaint(true);
            renderer.setUseOutlinePaint(true);
            renderer.setSeriesOutlinePaint(0,Color.gray);
            renderer.setBasePaint(Color.gray);
            renderer.setSeriesFillPaint(0,Color.gray);
            renderer.setSeriesShape(0, ShapeUtilities.createDiamond(2.0f));

            // "1" is the line plot
            renderer.setSeriesLinesVisible(1, true);
            renderer.setSeriesShapesVisible(1, false);

            // "0" is the scatter plot
            renderer.setSeriesLinesVisible(0, false);
            renderer.setSeriesShapesVisible(0, true);
        }

        NumberAxis xAxis = (NumberAxis) plot.getDomainAxis();
        xAxis.setNumberFormatOverride(NumberFormat.getPercentInstance());

        NumberAxis yAxis = (NumberAxis) plot.getRangeAxis();
        yAxis.setNumberFormatOverride(NumberFormat.getPercentInstance());

        return chart;

    }

    public static XYSeriesCollection getCAPMDataset(String symbol, int year) {
        Date current = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(current);
        c.add(Calendar.YEAR, -1 * year);
        Date start = c.getTime();
        ArrayList<HistoricalData> stockData = null;
        ArrayList<HistoricalData> indexData = null;
        try {
            stockData = DBUtil.getHistoricalDataByRange(symbol, start, current);
            indexData = DBUtil.getHistoricalDataByRange("SPY", start, current);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        double preStockPrice = Objects.requireNonNull(stockData).get(0).getClosePrice();
        double preIndexPrice = Objects.requireNonNull(indexData).get(0).getClosePrice();
        ArrayList<Double> stockReturns = new ArrayList<Double>();
        ArrayList<Double> indexReturns = new ArrayList<Double>();
        for(int i = 1; i<stockData.size(); i++){
            HistoricalData data = stockData.get(i);
            double ror = data.getClosePrice()/preStockPrice - 1;
            preStockPrice = data.getClosePrice();
            stockReturns.add(ror);
        }
        for(int i = 1; i<indexData.size(); i++){
            HistoricalData data = indexData.get(i);
            double ror = data.getClosePrice()/preIndexPrice - 1;
            preIndexPrice = data.getClosePrice();
            indexReturns.add(ror);
        }
        int size = Math.min(stockReturns.size(), indexReturns.size());
        if (stockReturns.size()>size) {
            stockReturns = new ArrayList<Double>(stockReturns.subList(0, size));
        }
        if (indexReturns.size()>size) {
            indexReturns = new ArrayList<Double>(indexReturns.subList(0, size));
        }
        ArrayList<Double> fitted = _findFittedValues(indexReturns, stockReturns);

        XYSeries s1 = new XYSeries("scattered");
        XYSeries s2 = new XYSeries("fitted");

        for (int i=0; i< indexReturns.size(); ++i) {
            s1.add(indexReturns.get(i), stockReturns.get(i));
            s2.add(indexReturns.get(i), fitted.get(i));
        }
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(s1);
        dataset.addSeries(s2);
        return dataset;
    }

}
