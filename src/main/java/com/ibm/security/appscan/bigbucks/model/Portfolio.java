package com.ibm.security.appscan.bigbucks.model;

import com.ibm.security.appscan.bigbucks.util.DBUtil;

import java.sql.SQLException;

public class Portfolio {
    private int portfolioId;
    private long accountId;
    private String symbol;
    private int share;
    private double avgFillPrice;
    private double amount;
    public String username;

    public String getShareName() {
        return shareName;
    }

    private String shareName;

    public Portfolio(int portfolioId, long accountId, String symbol, int share,
                     double avgFillPrice, double amount, String shareName){
        this.portfolioId = portfolioId;
        this.accountId = accountId;
        this.symbol = symbol;
        this.share = share;
        this.avgFillPrice = avgFillPrice;
        this.amount = amount;
        this.shareName = shareName;
    }
    public Portfolio(int portfolioId, long accountId, String symbol, int share,
                     double avgFillPrice, double amount, String shareName, String username){
        this.portfolioId = portfolioId;
        this.accountId = accountId;
        this.symbol = symbol;
        this.share = share;
        this.avgFillPrice = avgFillPrice;
        this.amount = amount;
        this.shareName = shareName;
        this.username = username;
    }


    public String getCurrentPriceStr(){
        double price = 0;
        try {
            price = DBUtil.getLastClosePrice(symbol);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return String.format("%.2f", price);
    }
    public double getCurrentPrice(){

        return Double.parseDouble(getCurrentPriceStr());

    }
    public String getAvgPriceStr(){
        double avg = amount/share;
        return String.format("%.2f", avg);
    }
    public double getAvgPrice(){
        return Double.parseDouble(getAvgPriceStr());
    }
    public double getCurrentValue(){
       return share*getCurrentPrice();
    }
    public String getTotalCostStr(){
        return String.format("%.2f", amount);
    }
    public String getCurrentValueStr(){
        return String.format("%.2f", getCurrentValue());
    }
    public String getProfitStr(){
        return String.format("%.2f", getProfit());
    }
    public double getProfit(){
        return getCurrentValue() - amount;
    }
    public int getPortfolioId() {
        return portfolioId;
    }

    public long getAccountId() {
        return accountId;
    }

    public String getSymbol() {
        return symbol;
    }
    public String getUsername() {
        return username;
    }

    public double getAmount() {
        return amount;
    }

    public double getAvgFillPrice() {
        return avgFillPrice;
    }

    public int getShare() {
        return share;
    }

}
