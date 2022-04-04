package com.ibm.security.appscan.bigbucks.dto;
import lombok.*;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.ibm.security.appscan.bigbucks.util.DBUtil;

import javax.swing.plaf.nimbus.State;

@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class StockDto {
    public String symbol;
    public String name;
    public BigDecimal price;
    public Timestamp date;

    public String sector;
    public String industry;
    public String country;

    public static List<StockDto> getHistoryStockDto(String symbol) throws SQLException {
        List<StockDto> stockdtos = new ArrayList<>();
        Connection connection = DBUtil.getConnection();
        Statement statement = connection.createStatement();

        ResultSet resultSet2 = statement.executeQuery("SELECT * FROM STOCKS WHERE SYMBOL = '"+symbol+"'");


        String name = "";
        String sector = "";
        String industry = "";
        String country = "";
        if(resultSet2.next()){
            name = resultSet2.getString("stock_name");
            sector = resultSet2.getString("sector");
            industry = resultSet2.getString("industry");
            country = resultSet2.getString("country");

        }
        statement.close();
        statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery("SELECT * FROM HISTORICALDATA WHERE SYMBOL = '"+symbol+"'");
        while (resultSet.next()){
            stockdtos.add(new StockDto(symbol, name,resultSet.getBigDecimal("price_close"),
                    resultSet.getTimestamp("date"),sector,industry,country));
        }
        return stockdtos;
    }
}
