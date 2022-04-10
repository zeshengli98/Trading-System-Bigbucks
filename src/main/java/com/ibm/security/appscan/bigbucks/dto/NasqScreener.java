package com.ibm.security.appscan.bigbucks.dto;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

import static com.ibm.security.appscan.bigbucks.util.DBUtil.getConnection;


public class NasqScreener {


    @CsvBindByName(column = "Symbol")
    private String symbol;

    @CsvBindByName(column = "Name")
    private String name;

    @CsvBindByName(column = "Country")
    private String country;

    @CsvBindByName(column = "Sector")
    private String sector;

    @CsvBindByName(column = "Industry")
    private String industry;

    public String getSymbol(){
        return symbol;
    }
    public String getName(){
        return name;
    }
    public String getCountry(){
        return country;
    }
    public String getSector(){
        return sector;
    }
    public String getIndustry(){
        return industry;
    }
    private static final String SAMPLE_CSV_FILE_PATH = "/resources/nasdaq_screener.csv";


    public static void screener() throws IOException, SQLException {
        Connection connection = getConnection();
        Statement statement = connection.createStatement();

        try (
                Reader reader = Files.newBufferedReader(Paths.get(SAMPLE_CSV_FILE_PATH));
        ) {
            CsvToBean<NasqScreener> csvToBean = new CsvToBeanBuilder(reader)
                    .withType(NasqScreener.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            Iterator<NasqScreener> csvUserIterator = csvToBean.iterator();

            while (csvUserIterator.hasNext()) {
                NasqScreener csvUser = csvUserIterator.next();
                String name = csvUser.getName();
                String symbol = csvUser.getSymbol();
                String sector = csvUser.getSector();
                String industry = csvUser.getIndustry();
                String country = csvUser.getCountry();
                System.out.println(symbol);
                try{
                    statement.execute("INSERT INTO STOCKS (SYMBOL,STOCK_NAME,SECTOR,INDUSTRY, COUNTRY) VALUES ('" + symbol + "', '" + name + "', '" + sector + "', '" + industry + "','" + country + "')");

                } catch (SQLException e) {

                }

            }
        }
    }

}


