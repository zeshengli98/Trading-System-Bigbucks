package com.ibm.security.appscan.bigbucks.yahoofinance;


import com.ibm.security.appscan.bigbucks.dto.StockDto;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class YahooStockAPI {
//    public StockDto getStock(String stockName) throws IOException {
//        StockDto dto = null;
//        Stock stock = YahooFinance.get(stockName);
//        dto=new StockDto(stock.getName(), stock.getQuote().getPrice());
//        return dto;
//    }

    public Map<String,Stock> getStock(String[] stockNames) throws IOException {
        Map<String,Stock> stock = YahooFinance.get(stockNames);
        return stock;
    }


    public List<HistoricalQuote> getHistory(String stockName, int year, String searchType) throws IOException {
        Calendar from = Calendar.getInstance();
        Calendar to = Calendar.getInstance();
        from.add(Calendar.YEAR, Integer.parseInt("-"+year));

        Stock stock = YahooFinance.get(stockName);

        List<HistoricalQuote> history = stock.getHistory(from,to, getInterval(searchType));
        return history;
    }

    public List<HistoricalQuote> getHistory(String stockName, Calendar from, Calendar to) throws IOException {
        Stock stock = YahooFinance.get(stockName);

        List<HistoricalQuote> history = stock.getHistory(from,to, getInterval("daily"));
        return history;
    }


    private String converDate(Calendar cal){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String format1 = format.format(cal.getTime());
        return format1;
    }

    private Interval getInterval(String searchType){
        Interval interval=null;
        switch (searchType.toUpperCase()){
            case "MONTHLY":
                interval = Interval.MONTHLY;
                break;
            case "WEEKLY":
                interval = Interval.WEEKLY;
                break;
            case "DAILY":
                interval = Interval.DAILY;
                break;
        }
        return interval;
    }

    public static void main(String[] args) throws IOException {
        YahooStockAPI yahooStockAPI = new YahooStockAPI();
//        System.out.println(yahooStockAPI.getStock("SPY"));

        yahooStockAPI.getHistory("SPY",1,"daily");


    }

}
