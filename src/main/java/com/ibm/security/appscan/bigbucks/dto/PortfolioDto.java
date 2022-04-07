package com.ibm.security.appscan.bigbucks.dto;

import com.ibm.security.appscan.bigbucks.util.DBUtil;
import lombok.*;

import java.sql.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;


@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class PortfolioDto {

    public LocalDate date;
    public String symbol;
    public long accountId;
    public int sharechg;
    public double amount;
    public double filledprice;
    public double marketprice;
    public double cashchg;
    public double assetchg;
    public double UnrealizedPnL;
    public double share;
    private static final DecimalFormat df = new DecimalFormat("0.00");
    public static ArrayList<PortfolioDto> getOrderFromTransaction(long accountId ,String symbol) throws SQLException {
        Connection connection = DBUtil.getConnection();

        String query = "SELECT * FROM TRANSACTIONS a join historicaldata b on a.SYMBOL = b.SYMBOL and a.DATE = b.DATE WHERE ACCOUNTID = ? AND a.SYMBOL = ? ORDER BY a.DATE";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setLong(1, accountId);
        ps.setString(2, symbol);
        ResultSet rs = ps.executeQuery();
        ArrayList<PortfolioDto> orders = new ArrayList<>();

        while (rs.next()){
            LocalDate date = rs.getTimestamp("DATE").toLocalDateTime().toLocalDate();

            int sharechg = rs.getInt("share");
            double filledprice = rs.getDouble("price");
            double marketprice = rs.getDouble("price_close");
            double amountchg = sharechg * filledprice;
            final double cashchg = -amountchg;
            final double assetchg = amountchg;
            orders.add(new PortfolioDto(date, symbol, accountId, sharechg, amountchg, filledprice,marketprice,cashchg,assetchg,0,0));

        }
        System.out.println(orders);

        ps.close();

        ArrayList<PortfolioDto> performance = new ArrayList<>();
        String query2 = "SELECT * FROM historicaldata WHERE SYMBOL = ? AND DATE >= (" +
                "                select min(DATE) from TRANSACTIONS where ACCOUNTID = ? AND SYMBOL = ?" +
                "    )  ORDER BY DATE";
        PreparedStatement ps2 = connection.prepareStatement(query2);
        ps2.setString(1,symbol);
        ps2.setLong(2,accountId);
        ps2.setString(3,symbol);
        ResultSet rs2 = ps2.executeQuery();
        int orderIndex = 0;
        int share = 0;
        double filledprice=0;
        double amount=0;
        while(rs2.next()){
            // get data from historical data
            double mktprice = rs2.getDouble("price_close");
            LocalDate date = rs2.getTimestamp("DATE").toLocalDateTime().toLocalDate();

            //get data from transation
            System.out.println(date.isEqual(orders.get(orderIndex).date));
            if(date.isEqual(orders.get(orderIndex).date)){
                double assetchg = orders.get(orderIndex).assetchg;
                double cashchg = orders.get(orderIndex).cashchg;
                int sharechg = orders.get(orderIndex).sharechg;
                System.out.println(sharechg);
//                filledprice = orders.get(orderIndex).filledprice;
                share+=sharechg;

                amount = share * mktprice;
//                amount = amount+assetchg;

                filledprice = assetchg/sharechg;
                double PnL = (mktprice-filledprice)*sharechg;
                double pctPnL = PnL/amount;
                // we do have transaction on that day
                performance.add(new PortfolioDto(date, symbol, accountId, sharechg, amount, filledprice,mktprice,cashchg,assetchg,PnL,share));
                System.out.println(new PortfolioDto(date, symbol, accountId, sharechg, amount, filledprice,mktprice,cashchg,assetchg,PnL,share));

                if(orderIndex<orders.size()-1) {
                    orderIndex = orderIndex+1;
                }
            }else{
                double PnL = (mktprice-filledprice)*share;
                double pctPnL = PnL/amount;
                amount = share * mktprice;
                performance.add(new PortfolioDto(date, symbol, accountId, share, amount, filledprice,mktprice,0,0,PnL,share));
                System.out.println(new PortfolioDto(date, symbol, accountId, share, amount, filledprice,mktprice,0,0,PnL,share));
            }
        }

        return performance;
    }

    public static ArrayList<PortfolioDto> getOrderFromAllTransaction(String symbol) throws SQLException {
        Connection connection = DBUtil.getConnection();

        String query = "SELECT * FROM TRANSACTIONS a join historicaldata b on a.SYMBOL = b.SYMBOL and a.DATE = b.DATE WHERE a.SYMBOL = ? ORDER BY a.DATE";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, symbol);
        ResultSet rs = ps.executeQuery();
        ArrayList<PortfolioDto> orders = new ArrayList<>();

        while (rs.next()){
            LocalDate date = rs.getTimestamp("DATE").toLocalDateTime().toLocalDate();

            int sharechg = rs.getInt("share");
            double filledprice = rs.getDouble("price");
            double marketprice = rs.getDouble("price_close");
            double amountchg = sharechg * filledprice;
            final double cashchg = -amountchg;
            final double assetchg = amountchg;
            orders.add(new PortfolioDto(date, symbol, 0, sharechg, amountchg, filledprice,marketprice,cashchg,assetchg,0,0));

        }
        System.out.println(orders);

        ps.close();

        ArrayList<PortfolioDto> performance = new ArrayList<>();
        String query2 = "SELECT * FROM historicaldata WHERE SYMBOL = ? AND DATE >= (select min(DATE) from TRANSACTIONS WHERE SYMBOL = ?)  ORDER BY DATE";
        PreparedStatement ps2 = connection.prepareStatement(query2);
        ps2.setString(1,symbol);
        ps2.setString(2,symbol);
        ResultSet rs2 = ps2.executeQuery();
        int orderIndex = 0;
        int share = 0;
        double filledprice=0;
        double amount=0;
        while(rs2.next()){
            // get data from historical data
            double mktprice = rs2.getDouble("price_close");
            LocalDate date = rs2.getTimestamp("DATE").toLocalDateTime().toLocalDate();

            //get data from transation
            System.out.println(date.isEqual(orders.get(orderIndex).date));
            if(date.isEqual(orders.get(orderIndex).date)){
                double assetchg = orders.get(orderIndex).assetchg;
                double cashchg = orders.get(orderIndex).cashchg;
                int sharechg = orders.get(orderIndex).sharechg;
                System.out.println(sharechg);
//                filledprice = orders.get(orderIndex).filledprice;
                share+=sharechg;

                amount = share * mktprice;
//                amount = amount+assetchg;

                filledprice = assetchg/sharechg;
                double PnL = (mktprice-filledprice)*sharechg;
                double pctPnL = PnL/amount;
                // we do have transaction on that day
                performance.add(new PortfolioDto(date, symbol, 0, sharechg, amount, filledprice,mktprice,cashchg,assetchg,PnL,share));
                System.out.println(new PortfolioDto(date, symbol, 0, sharechg, amount, filledprice,mktprice,cashchg,assetchg,PnL,share));

                if(orderIndex<orders.size()-1) {
                    orderIndex = orderIndex+1;
                }
            }else{
                double PnL = (mktprice-filledprice)*share;
                double pctPnL = PnL/amount;
                amount = share * mktprice;
                performance.add(new PortfolioDto(date, symbol, 0, share, amount, filledprice,mktprice,0,0,PnL,share));
                System.out.println(new PortfolioDto(date, symbol, 0, share, amount, filledprice,mktprice,0,0,PnL,share));
            }
        }

        return performance;
    }

    public static void main(String[] args) throws SQLException {

//        getOrderFromTransaction(800000, "AAPL");

        getOrderFromAllTransaction("AAPL");
    }

}