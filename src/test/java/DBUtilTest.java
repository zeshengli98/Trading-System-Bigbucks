
import com.ibm.security.appscan.bigbucks.model.Account;
import com.ibm.security.appscan.bigbucks.model.Portfolio;
import com.ibm.security.appscan.bigbucks.model.Stock;
import com.ibm.security.appscan.bigbucks.util.DBUtil;
import junit.framework.TestCase;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;


import static com.ibm.security.appscan.bigbucks.dto.StockDto.getHistoryStockDto;
import static com.ibm.security.appscan.bigbucks.util.DBUtil.*;

public class DBUtilTest extends TestCase {

    @Test
    public void HistoricalTest() throws SQLException {
            printDB("historicaldata");
        }




    @Test
    public void PeopleTest() throws SQLException {
//        BigbucksDBUtil db = new BigbucksDBUtil();
        Connection connection = getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM PEOPLE ");

        while (resultSet.next()) {
            String id = resultSet.getString("USER_ID");
            String password = resultSet.getString("PASSWORD");
            String firstname = resultSet.getString("FIRST_NAME");
            String lastname = resultSet.getString("LAST_NAME");
            String role = resultSet.getString("ROLE");
            System.out.println("user_id: " + id + " password: " + password
                    + " firstname: " + firstname + " lastname: " + lastname + " role: " + role);

        }

    }

    @Test
    public void AccountTest() throws SQLException {
        printDB("accounts");
    }
    @Test
    public void StocksTest() throws SQLException {
        printDB("stocks");
    }

    @Test
    public void FeedbackTest() throws SQLException {
//        BigbucksDBUtil db = new BigbucksDBUtil();
        printDB("feedback");

    }

    @Test
    public void CreateAccountTest() throws SQLException {
//        Connection connection = getconnection();
//        Statement statement = connection.createStatement();
        String error = createAccount("user1", "user2",  "zsasda", "lll");
        System.out.println(error);
        PeopleTest();
    }

    @Test
    public void changePwdTest() throws SQLException {

        String error = changePassword("user1", "user10086");
        System.out.println(error);
        PeopleTest();
    }
    @Test
    public void orderTest() throws SQLException {

        Account[] accounts = getAccounts("admin1");
        Account account = accounts[0];
        Long id = account.getAccountId();
        String error = orderStocks("admin1", id, "buy", "ADBE", 100, 10000000);
        String error2 = orderStocks("admin1", id, "buy", "ADBE", 200, 1000);
        String error3 = orderStocks("admin1", id, "buy", "AAPL", 100, 100.00);
        String error4 = orderStocks("admin1", id, "buy", "AAPL", 200, 100.00);

        String error5 = orderStocks("admin1", id, "sell", "AAPL", 100, 100.00);
        String error6 = orderStocks("admin1", id, "sell", "AAPL", 200, 100.00);
        String error7 = orderStocks("admin1", id, "sell", "ADBE", 100, 1000);
        String error8 = orderStocks("admin1", id, "sell", "ADBE", 200, 1000);
//        System.out.println(error6);
        printDB("Transactions");
        printDB("portfolios");
        printDB("accounts");
    }

    @Test
    public void orderTest2() throws SQLException {

        Account[] accounts = getAccounts("123");
        Account account = accounts[0];
        Long id = account.getAccountId();
        String error = orderStocks("123", id, "buy", "123", 100, 10);

        printDB("Transactions");
        printDB("portfolio");
        printDB("accounts");
    }


    @Test
    public void getStocksTest() throws SQLException {
        Stock[] stocks = getStocks();
        for(int i=0;i<stocks.length;i++){
            Stock stock = stocks[i];
            System.out.println(stock.getStockSymbol());
        }


    }

    @Test
    public void getHistoricalDataTest() throws SQLException, IOException {
        getHistoricalData("SPY",5, "daily");
    }

    @Test
    public void fetchallDataTest() throws SQLException, IOException {
        fetchAllData();
    }

    @Test
    public void getStocksInDBTest() throws SQLException {
        String[] stocks = getStocksInDB();
        for(int i=0;i<stocks.length;i++){
            String stock = stocks[i];
            System.out.println(i);
            System.out.println(getStockLastDate(stock));
        }

    }

    @Test void TestWhatever() throws SQLException, IOException {
        java.util.Formatter formatter = new java.util.Formatter();
        System.out.println(String.format("%.2f", 10.213*100)+"%");
    }

    @Test void TestWhatever2() throws SQLException, IOException {
        System.out.println(getAllUsersPortfolios().get(0).username);
    }
    @Test void TestWhatever3() throws SQLException, IOException {
        long accountId = DBUtil.getAccounts("admin1")[0].getAccountId();
        ArrayList<Portfolio> holdings = DBUtil.getPortfoliosByAccount(accountId);
        System.out.println(holdings);
    }

}