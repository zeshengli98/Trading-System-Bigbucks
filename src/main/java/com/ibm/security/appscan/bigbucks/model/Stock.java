package com.ibm.security.appscan.bigbucks.model;

import com.ibm.security.appscan.bigbucks.util.DBUtil;

import java.sql.SQLException;

public class Stock {
    public String symbol;
    public String fullname;
    public String sector;
    public String industry;
    public String country;

    public Stock(String symbol, String name, String sector, String industry, String country){
        this.symbol = symbol;
        this.fullname = name;
        this.sector = sector;
        this.industry = industry;
        this.country = country;
    }


    public static Stock getStock(String symbol) throws SQLException {
        return DBUtil.getStock(symbol);
    }

    public static Stock[] getStocks() throws SQLException {
        try {
            return DBUtil.getStocks();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return new Stock[0];
    }

    public String getStockName(){return this.fullname;}
    public String getStockSector(){return this.sector;}
    public String getStockIndustry(){return this.industry;}
    public String getStockCountry(){return this.country;}
    public String getStockSymbol(){return this.symbol;}


}
