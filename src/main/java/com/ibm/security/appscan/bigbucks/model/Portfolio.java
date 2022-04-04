package com.ibm.security.appscan.bigbucks.model;

public class Portfolio {
    private int portfolioId;
    private long accountId;
    private String symbol;
    private int share;
    private double avgFillPrice;
    private double amount;

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


    public int getPortfolioId() {
        return portfolioId;
    }

    public long getAccountId() {
        return accountId;
    }

    public String getSymbol() {
        return symbol;
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
