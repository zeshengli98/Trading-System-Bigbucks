package com.ibm.security.appscan.bigbucks.dto;


import com.ibm.security.appscan.bigbucks.util.DBUtil;
import lombok.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.ibm.security.appscan.bigbucks.dto.PortfolioDto.getOrderFromAllTransaction;
import static com.ibm.security.appscan.bigbucks.dto.PortfolioDto.getOrderFromTransaction;

@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class RiskReturnProfileDto {
    public LocalDate date;
    @Setter
    public double cash;
    @Setter
    public double asset;
    @Setter
    public double equity;
    @Setter
    public double PnL;
    @Setter
    public double pctPnL;



    public static ArrayList<RiskReturnProfileDto> getRiskRetHistory(long accountId) throws SQLException {
        Connection connection = DBUtil.getConnection();
        String query = "SELECT DISTINCT DATE FROM historicaldata WHERE DATE >= (select min(DATE) from TRANSACTIONS where ACCOUNTID = ?)  ORDER BY DATE";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setLong(1, accountId);
        ResultSet rs = ps.executeQuery();
        ArrayList<RiskReturnProfileDto> profiles = new ArrayList<>();
        while(rs.next()){
            LocalDate date = rs.getTimestamp("date").toLocalDateTime().toLocalDate();
            profiles.add(new RiskReturnProfileDto(date, DBUtil.initMoney,0,DBUtil.initMoney,0,0));

        }
        ps.close();
        String query2 = "SELECT DISTINCT SYMBOL FROM transactions where ACCOUNTID = ?";
        PreparedStatement ps2 = connection.prepareStatement(query2);
        ps2.setLong(1,accountId);
        ResultSet rs2 = ps2.executeQuery();

//        double assetValue = 0;
        while(rs2.next()){
            String symbol = rs2.getString("SYMBOL");
            List<PortfolioDto> aPerformance =getOrderFromTransaction(accountId,symbol);

            int profilesIx = 0;
            int performIx = 0;
            double cashcon = 0;
            double lastEquity = 1000000;
            while(profilesIx<profiles.size() && performIx<aPerformance.size()){

                LocalDate aDate =  profiles.get(profilesIx).date;
                LocalDate bDate = aPerformance.get(performIx).date;
                if (aDate.isEqual(bDate) ){
                    if (aPerformance.get(performIx).cashchg!=0) {
                        cashcon =cashcon+ aPerformance.get(performIx).cashchg;
                    }
                    double cash = profiles.get(profilesIx).cash + cashcon;
//                    assetValue = assetValue + aPerformance.get(performIx).assetchg;

                    profiles.get(profilesIx).setCash(cash);
                    profiles.get(profilesIx).setAsset(profiles.get(profilesIx).asset + aPerformance.get(performIx).amount);
                    profiles.get(profilesIx).setEquity(profiles.get(profilesIx).cash + profiles.get(profilesIx).asset);

                    double equity = profiles.get(profilesIx).equity;
                    profiles.get(profilesIx).setPnL(equity - lastEquity);
                    profiles.get(profilesIx).setPctPnL(profiles.get(profilesIx).PnL / lastEquity);
                    lastEquity = equity;

                    profilesIx++;
                    performIx++;

                }else{
                    profilesIx++;
                }
            }
        }
//        profiles.remove(0);
        return profiles;
    }

    public static ArrayList<RiskReturnProfileDto> getAllUsersRiskRetHistory() throws SQLException {
        Connection connection = DBUtil.getConnection();
        String query0 = "SELECT DISTINCT ACCOUNT_ID FROM ACCOUNTS";
        PreparedStatement ps0 = connection.prepareStatement(query0);
        ResultSet rs0 = ps0.executeQuery();
        double initMoney = 0;
        while(rs0.next()){
            initMoney=initMoney+1000000;
        }


        String query = "SELECT DISTINCT DATE FROM historicaldata WHERE DATE >= (select min(DATE) from TRANSACTIONS )  ORDER BY DATE";
        PreparedStatement ps = connection.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        ArrayList<RiskReturnProfileDto> profiles = new ArrayList<>();
        while(rs.next()){
            LocalDate date = rs.getTimestamp("date").toLocalDateTime().toLocalDate();
            profiles.add(new RiskReturnProfileDto(date, initMoney,0,initMoney,0,0));

        }
        ps.close();
        String query2 = "SELECT DISTINCT SYMBOL FROM transactions";
        PreparedStatement ps2 = connection.prepareStatement(query2);
        ResultSet rs2 = ps2.executeQuery();

//        double assetValue = 0;
        while(rs2.next()){
            String symbol = rs2.getString("SYMBOL");
            List<PortfolioDto> aPerformance =getOrderFromAllTransaction(symbol);

            int profilesIx = 0;
            int performIx = 0;
            double cashcon = 0;
            double lastEquity = 1000000;
            while(profilesIx<profiles.size() && performIx<aPerformance.size()){

                LocalDate aDate =  profiles.get(profilesIx).date;
                LocalDate bDate = aPerformance.get(performIx).date;
                if (aDate.isEqual(bDate) ){
                    if (aPerformance.get(performIx).cashchg!=0) {
                        cashcon =cashcon+ aPerformance.get(performIx).cashchg;
                    }
                    double cash = profiles.get(profilesIx).cash + cashcon;
//                    assetValue = assetValue + aPerformance.get(performIx).assetchg;

                    profiles.get(profilesIx).setCash(cash);
                    profiles.get(profilesIx).setAsset(profiles.get(profilesIx).asset + aPerformance.get(performIx).amount);
                    profiles.get(profilesIx).setEquity(profiles.get(profilesIx).cash + profiles.get(profilesIx).asset);

                    double equity = profiles.get(profilesIx).equity;
                    profiles.get(profilesIx).setPnL(equity - lastEquity);
                    profiles.get(profilesIx).setPctPnL(profiles.get(profilesIx).PnL / lastEquity);
                    lastEquity = equity;

                    profilesIx++;
                    performIx++;

                }else{
                    profilesIx++;
                }
            }
        }
        double lasteq = initMoney;
        for(int i=0;i<profiles.size();i++){
            double eq = profiles.get(i).equity;
            profiles.get(i).setPnL(eq-lasteq);
            profiles.get(i).setPctPnL((eq-lasteq)/lasteq);
            lasteq = eq;
        }
//        profiles.remove(0);
        return profiles;
    }


    public static double calAvgRet(ArrayList<RiskReturnProfileDto> profile){
        double retsum = 0;
        for(int i=0;i<profile.size();i++){
            retsum = retsum + profile.get(i).pctPnL;
        }
        double avgret = retsum/profile.size();
        return avgret;
    }

    public static double calStd(ArrayList<RiskReturnProfileDto> profile){
        double stdsum = 0;
        double avgret = calAvgRet(profile);
        for(int i=0;i<profile.size();i++){
            stdsum = stdsum + (profile.get(i).pctPnL-avgret)*(profile.get(i).pctPnL-avgret);
        }
        double std = Math.sqrt(stdsum/profile.size());
        return std;
    }

    public static double calSharpeRatio(ArrayList<RiskReturnProfileDto> profile){
        double rf = 0.02067;
        double avgret = calAvgRet(profile)*252;
        double std = calStd(profile)*Math.sqrt(252);
        double sharpeRatio = (avgret-rf)/std;
        return sharpeRatio;


    }
    public static void main(String[] args) throws SQLException {
        ArrayList<RiskReturnProfileDto> rr =  getAllUsersRiskRetHistory();
        for (int i=0;i<rr.size();i++){
            System.out.println(rr.get(i));
        }
        System.out.println(calAvgRet(rr)*252);
        System.out.println(calStd(rr)*Math.sqrt(252));
        System.out.println(calSharpeRatio(rr));
    }
}