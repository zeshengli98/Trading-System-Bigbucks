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
        double cash = 1000000;
//        double assetValue = 0;
        while(rs2.next()){
            String symbol = rs2.getString("SYMBOL");
            List<PortfolioDto> aPerformance =getOrderFromTransaction(accountId,symbol);

            int profilesIx = 0;
            int performIx = 0;

            double lastEquity = 1000000;
            while(profilesIx<profiles.size() && performIx<aPerformance.size()){

                LocalDate aDate =  profiles.get(profilesIx).date;
                LocalDate bDate = aPerformance.get(performIx).date;
                if (aDate.isEqual(bDate) ){
                    cash = cash + aPerformance.get(performIx).cashchg;
//                    assetValue = assetValue + aPerformance.get(performIx).assetchg;

                    profiles.get(profilesIx).setCash(cash);
                    profiles.get(profilesIx).setAsset(profiles.get(profilesIx).asset + aPerformance.get(performIx).amount);
                    profiles.get(profilesIx).setEquity(profiles.get(profilesIx).cash+profiles.get(profilesIx).asset);

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

        return profiles;
    }

    public static void main(String[] args) throws SQLException {
        ArrayList<RiskReturnProfileDto> rr =  getRiskRetHistory(800000);
        for (int i=0;i<rr.size();i++){
            System.out.println(rr.get(i));
        }

    }
}