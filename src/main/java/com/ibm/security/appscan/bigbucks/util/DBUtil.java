/**
 * This application is for demonstration use only. It contains known application security
 * vulnerabilities that were created expressly for demonstrating the functionality of
 * application security testing tools. These vulnerabilities may present risks to the
 * technical environment in which the application is installed. You must delete and
 * uninstall this demonstration application upon completion of the demonstration for
 * which it is intended.
 * <p>
 * IBM DISCLAIMS ALL LIABILITY OF ANY KIND RESULTING FROM YOUR USE OF THE APPLICATION
 * OR YOUR FAILURE TO DELETE THE APPLICATION FROM YOUR ENVIRONMENT UPON COMPLETION OF
 * A DEMONSTRATION. IT IS YOUR RESPONSIBILITY TO DETERMINE IF THE PROGRAM IS APPROPRIATE
 * OR SAFE FOR YOUR TECHNICAL ENVIRONMENT. NEVER INSTALL THE APPLICATION IN A PRODUCTION
 * ENVIRONMENT. YOU ACKNOWLEDGE AND ACCEPT ALL RISKS ASSOCIATED WITH THE USE OF THE APPLICATION.
 * <p>
 * IBM AltoroJ
 * (c) Copyright IBM Corp. 2008, 2013 All Rights Reserved.
 */

package com.ibm.security.appscan.bigbucks.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.Date;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.ibm.security.appscan.Log4Bigbucks;
import com.ibm.security.appscan.bigbucks.dto.NasqScreener;
import com.ibm.security.appscan.bigbucks.model.*;
import com.ibm.security.appscan.bigbucks.model.User.Role;
import com.ibm.security.appscan.bigbucks.yahoofinance.YahooStockAPI;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;

/**
 * Utility class for database operations
 * @author Alexei
 *
 */
public class DBUtil {
    public static final double initMoney = 1000000;
    private static final String PROTOCOL = "jdbc:derby:";
    private static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    private static final String DBNAME = "bigbucks";

    public static final String CREDIT_CARD_ACCOUNT_NAME = "Credit Card";
    public static final String CHECKING_ACCOUNT_NAME = "Checking";
    public static final String SAVINGS_ACCOUNT_NAME = "Savings";

    public static final double CASH_ADVANCE_FEE = 2.50;

    private static DBUtil instance = null;
    private Connection connection = null;
    private DataSource dataSource = null;

    //private constructor
    private DBUtil() {
        /*
         **
         **			Default location for the database is current directory:
         */

        String dataSourceName = ServletUtil.getAppProperty("database.alternateDataSource");

        /* Connect to an external database (e.g. DB2) */
        if (dataSourceName != null && dataSourceName.trim().length() > 0) {
            try {
                Context initialContext = new InitialContext();
                Context environmentContext = (Context) initialContext.lookup("java:comp/env");
                dataSource = (DataSource) environmentContext.lookup(dataSourceName.trim());
            } catch (Exception e) {
                e.printStackTrace();
                Log4Bigbucks.getInstance().logError(e.getMessage());
            }

            /* Initialize connection to the integrated Apache Derby DB*/
        } else {
            System.setProperty("derby.system.home", System.getProperty("user.home") + "/bigbucks/");
            System.out.println("Derby Home=" + System.getProperty("derby.system.home"));

            try {
                //load JDBC driver
                Class.forName(DRIVER).newInstance();
            } catch (Exception e) {
                Log4Bigbucks.getInstance().logError(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static Connection getConnection() throws SQLException {

        if (instance == null)
            instance = new DBUtil();

        if (instance.connection == null || instance.connection.isClosed()) {

            //If there is a custom data source configured use it to initialize
            if (instance.dataSource != null) {
                instance.connection = instance.dataSource.getConnection();

                if (ServletUtil.isAppPropertyTrue("database.reinitializeOnStart")) {
                    instance.initDB();
                }
                return instance.connection;
            }

            // otherwise initialize connection to the built-in Derby database
            try {
                //attempt to connect to the database
                instance.connection = DriverManager.getConnection(PROTOCOL + DBNAME);

                if (ServletUtil.isAppPropertyTrue("database.reinitializeOnStart")) {
                    instance.initDB();
                }
            } catch (SQLException e) {
                //if database does not exist, create it an initialize it
                if (e.getErrorCode() == 40000) {
                    instance.connection = DriverManager.getConnection(PROTOCOL + DBNAME + ";create=true");
                    instance.initDB();
                    //otherwise pass along the exception
                } else {
                    throw e;
                }
            }

        }

        return instance.connection;
    }

    /*
     * Create and initialize the database
     */

    public void initDB() throws SQLException {
        Statement statement = connection.createStatement();
        try {
            statement.execute("DROP TABLE PEOPLE");
        } catch (SQLException ex) {}
        try {
            statement.execute("DROP TABLE FEEDBACK");
        } catch (SQLException ex) {}
        try {
            statement.execute("DROP TABLE ACCOUNTS");
        } catch (SQLException ex) {}
            try {
            statement.execute("DROP TABLE TRANSACTIONS");
            } catch (SQLException ex) {}
                try {
            statement.execute("DROP TABLE HISTORICALDATA");
                } catch (SQLException ex) {}
                    try {
            statement.execute("DROP TABLE PORTFOLIOS");
                    } catch (SQLException ex) {}
                        try {
            statement.execute("DROP TABLE STOCKS");
                        } catch (SQLException ex) {}

        statement.execute("CREATE TABLE PEOPLE (USER_ID VARCHAR(50) NOT NULL, PASSWORD VARCHAR(20) NOT NULL,FIRST_NAME VARCHAR(100) NOT NULL, LAST_NAME VARCHAR(100) NOT NULL,ROLE VARCHAR(50) NOT NULL, PRIMARY KEY (USER_ID))");
        statement.execute("CREATE TABLE ACCOUNTS (ACCOUNT_ID BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY (START WITH 800000, INCREMENT BY 1), USERID VARCHAR(50) NOT NULL, ACCOUNT_NAME VARCHAR(100) NOT NULL, BALANCE DOUBLE NOT NULL, PRIMARY KEY (ACCOUNT_ID))");
        statement.execute("CREATE TABLE HISTORICALDATA (DATA_ID int not null generated always as identity, SYMBOL varchar(5), DATE TIMESTAMP NOT NULL,PRICE_OPEN DOUBLE NOT NULL, PRICE_HIGH DOUBLE NOT NULL, PRICE_LOW DOUBLE NOT NULL, PRICE_CLOSE DOUBLE NOT NULL, PRIMARY KEY(SYMBOL, DATE))");
        statement.execute("CREATE TABLE TRANSACTIONS (TRANSACTION_ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 0, INCREMENT BY 1), ACCOUNTID BIGINT NOT NULL, DATE TIMESTAMP NOT NULL, SYMBOL VARCHAR(50), SHARE INTEGER NOT NULL, PRICE DOUBLE NOT NULL, TYPE VARCHAR(100) NOT NULL, AMOUNT DOUBLE NOT NULL, PRIMARY KEY (TRANSACTION_ID))");
        statement.execute("CREATE TABLE PORTFOLIOS (PORTFOLIO_ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), ACCOUNTID BIGINT NOT NULL, SYMBOL VARCHAR(50) NOT NULL, SHARE INTEGER NOT NULL, AVG_FILL_PRICE DOUBLE NOT NULL, AMOUNT DOUBLE NOT NULL)");
//        statement.execute("CREATE TABLE PORTFOLIOS (PORTFOLIO_ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), ACCOUNTID BIGINT NOT NULL, STOCKID VARCHAR(50) NOT NULL, SHARE INTEGER NOT NULL, BUYIN_PRICE DOUBLE NOT NULL, PRIMARY KEY (ACCOUNTID, STOCKID))");
        statement.execute("CREATE TABLE STOCKS (SYMBOL VARCHAR(50) NOT NULL, STOCK_NAME VARCHAR(1000) NOT NULL, SECTOR VARCHAR(500) , INDUSTRY VARCHAR(500),COUNTRY VARCHAR(200), PRIMARY KEY (SYMBOL))");
        statement.execute("CREATE TABLE FEEDBACK (FEEDBACK_ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1022, INCREMENT BY 1), NAME VARCHAR(100) NOT NULL, EMAIL VARCHAR(50) NOT NULL, SUBJECT VARCHAR(100) NOT NULL, COMMENTS VARCHAR(500) NOT NULL, PRIMARY KEY (FEEDBACK_ID))");


        // Create 'admin' account to control all the users.
        statement.execute("INSERT INTO PEOPLE (USER_ID,PASSWORD,FIRST_NAME,LAST_NAME,ROLE) VALUES ('admin1', 'admin1', 'Admin', '1','admin1')");
        statement.execute("INSERT INTO ACCOUNTS (USERID,ACCOUNT_NAME,BALANCE) VALUES ('admin1','admin1', 1000000)");

        System.out.println("New derby database created.");
        Log4Bigbucks.getInstance().logInfo("Database initialized");

        NasqScreener nasqScreener = new NasqScreener();
        try {
            nasqScreener.screener();
            System.out.println("Table stock data inserted.");
            Log4Bigbucks.getInstance().logInfo("Database initialized");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * Create user account
     */
    public static String createAccount (String username, String password, String firstname, String lastname) {
        String error_adduser = addUser(username, password, firstname, lastname);

        if (error_adduser != null){
            return error_adduser;
        }

        String error_addaccount = addAccount(username, "stock");
        if (error_addaccount != null){
            return error_addaccount;
        }
        Log4Bigbucks.getInstance().logInfo("Successfully Create Account for: " + username);
        return null;
    }

    /**
     * Retrieve feedback details
     * @param feedbackId specific feedback ID to retrieve or Feedback.FEEDBACK_ALL to retrieve all stored feedback submissions
     */
    public static ArrayList<Feedback> getFeedback(long feedbackId) {
        ArrayList<Feedback> feedbackList = new ArrayList<Feedback>();

        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();

            String query = "SELECT * FROM FEEDBACK";

            if (feedbackId != Feedback.FEEDBACK_ALL) {
                query = query + " WHERE FEEDBACK_ID = " + feedbackId + "";
            }

            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String name = resultSet.getString("NAME");
                String email = resultSet.getString("EMAIL");
                String subject = resultSet.getString("SUBJECT");
                String message = resultSet.getString("COMMENTS");
                long id = resultSet.getLong("FEEDBACK_ID");
                Feedback feedback = new Feedback(id, name, email, subject, message);
                feedbackList.add(feedback);
            }
        } catch (SQLException e) {
            Log4Bigbucks.getInstance().logError("Error retrieving feedback: " + e.getMessage());
        }

        return feedbackList;
    }


    /**
     * Authenticate user
     * @param user user name
     * @param password password
     * @return true if valid user, false otherwise
     * @throws SQLException
     */
    public static boolean isValidUser(String user, String password) throws SQLException {
        if (user == null || password == null || user.trim().length() == 0 || password.trim().length() == 0)
            return false;

        Connection connection = getConnection();
        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery("SELECT COUNT(*)FROM PEOPLE WHERE USER_ID = '" + user + "' AND PASSWORD='" + password + "'"); /* BAD - user input should always be sanitized */

        if (resultSet.next()) {

            if (resultSet.getInt(1) > 0)
                return true;
        }
        return false;
    }

    /**
     * Authenticate user function 2
     * @param user user name
     * @return true if user exist already, false otherwise
     * @throws SQLException
     */
    public static boolean isExistedUser(String user) throws SQLException {
        if (user == null || user.trim().length() == 0)
            return false;

        Connection connection = getConnection();
        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery("SELECT COUNT(*)FROM PEOPLE WHERE USER_ID = '" + user + "'");

        if (resultSet.next()) {

            if (resultSet.getInt(1) > 0)
                return true;
        }
        return false;
    }


    /**
     * Get user information
     * @param username
     * @return user information
     * @throws SQLException
     */
    public static User getUserInfo(String username) throws SQLException {
        if (username == null || username.trim().length() == 0)
            return null;

        Connection connection = getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT FIRST_NAME,LAST_NAME,ROLE FROM PEOPLE WHERE USER_ID = '" + username + "' "); /* BAD - user input should always be sanitized */

        String firstName = null;
        String lastName = null;
        String roleString = null;
        if (resultSet.next()) {
            firstName = resultSet.getString("FIRST_NAME");
            lastName = resultSet.getString("LAST_NAME");
            roleString = resultSet.getString("ROLE");
        }

        if (firstName == null || lastName == null)
            return null;

        User user = new User(username, firstName, lastName);

        if (roleString.equalsIgnoreCase("admin"))
            user.setRole(Role.Admin);

        return user;
    }


    public static Stock getStock(String symbol) throws SQLException {
        Connection connection = getConnection();
        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery("SELECT * FROM STOCKS WHERE SYMBOL = '"+symbol+"'");
        if (resultSet.next()){
            return new Stock(resultSet.getString("symbol"),resultSet.getString("stock_name"),
                    resultSet.getString("sector"),resultSet.getString("industry"),resultSet.getString("country"));
        }
        return null;
    }

    public static Stock[] getStocks() throws SQLException {
        Connection connection = getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM STOCKS");
        ArrayList<Stock> stocks = new ArrayList<Stock>();
        while(resultSet.next()){
            Stock stock = new Stock(resultSet.getString("symbol"),resultSet.getString("stock_name"),
                    resultSet.getString("sector"),resultSet.getString("industry"),resultSet.getString("country"));
            stocks.add(stock);
        }
        return stocks.toArray(new Stock[stocks.size()]);
    }

    public static ArrayList<HistoricalData> getHistoricalDataByRange(String symbol, Date start, Date end) throws SQLException {
        ArrayList<HistoricalData> result = new ArrayList<HistoricalData>();
        try{
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String startDate = format.format(start);
            String endDate = format.format(end);
            System.out.println(startDate);
            String query = "select * from HISTORICALDATA where SYMBOL=" + "'"
                    +  symbol + "' AND " + "DATE between timestamp('"
                    + startDate + "','00:00:00') and timestamp('" +
                    endDate + "', '00:00:00') order by date";
            System.out.println(query);
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()){
                HistoricalData data = new HistoricalData();
                Timestamp ts = rs.getTimestamp("date");
                data.setDate(ts);
                data.setClosePrice(rs.getDouble("price_close"));
                data.setHighPrice(rs.getDouble("price_high"));
                data.setId(rs.getInt("DATA_ID"));
                data.setLowPrice(rs.getDouble("PRICE_LOW"));
                data.setSymbol(rs.getString("symbol"));
                data.setOpenPrice(rs.getDouble("PRICE_OPEN"));
                result.add(data);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }

    public static ArrayList<Double> getReturnByRange(String symbol, Date start, Date end) throws SQLException {
        ArrayList<Double> res = new ArrayList<Double>();
        ArrayList<HistoricalData> historicalData = getHistoricalDataByRange(symbol, start, end);
        for(int i = 0; i<historicalData.size(); i++){

        }
        return res;
    }

    public static ArrayList<Portfolio> getPortfoliosByAccount(long accountID){
        ArrayList<Portfolio> result = new ArrayList<Portfolio>();
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();

            String query = "SELECT * FROM PORTFOLIOS a join STOCKS b on a.SYMBOL=b.SYMBOL WHERE share > 0 AND ACCOUNTID="+accountID;
            System.out.println(query);
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {

                int pid = resultSet.getInt("portfolio_id");
                int aid = resultSet.getInt("accountid");
                String symbol = resultSet.getString("symbol");
                int share = resultSet.getInt("share");
                double avgFillPrice = resultSet.getDouble("avg_fill_price");
                double amount = resultSet.getDouble("amount");
                String shareName = resultSet.getString("STOCK_NAME");
                //Feedback feedback = new Feedback(id, name, email, subject, message);
                Portfolio portfolio = new Portfolio(pid, aid, symbol, share, avgFillPrice, amount, shareName);
                result.add(portfolio);
            }
        } catch (SQLException e) {
            Log4Bigbucks.getInstance().logError("Error retrieving portfolios: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }


    public static List<Portfolio> getCurrentOrder() throws SQLException {
        Connection connection = getConnection();
        Statement statement = connection.createStatement();

        ResultSet rs = statement.executeQuery("SELECT * FROM TRANSACTIONS a join STOCKS b on a.SYMBOL = b.SYMBOL join ACCOUNTS c on a.ACCOUNTID=c.ACCOUNT_ID");
        LocalDate curr = LocalDate.now();
        List<Portfolio> currPort = new ArrayList<>();
        while(rs.next()){
            int pid = rs.getInt("TRANSACTION_ID");
            long accountId = rs.getLong("accountID");
            String symbol = rs.getString("symbol");
            int share = rs.getInt("share");
            double price = rs.getDouble("price");
            double amount = rs.getDouble("amount");
            String shareName = rs.getString("STOCK_NAME");
            String username = rs.getString("userid");
            LocalDate date = rs.getTimestamp("date").toLocalDateTime().toLocalDate();
            if (date.isEqual(curr)){
                currPort.add(new Portfolio(pid, accountId, symbol, share,  price, amount, shareName, username));
            }

        }
        return currPort;
    }


    public static List<Portfolio> getOrdersByAccount(long accountId) throws SQLException {
        Connection connection = getConnection();

        String query = "SELECT * FROM TRANSACTIONS a join STOCKS b on a.SYMBOL = b.SYMBOL join ACCOUNTS c on a.ACCOUNTID=c.ACCOUNT_ID WHERE ACCOUNTID = ? ORDER BY DATE DESC ";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setLong(1,accountId);
        ResultSet rs = ps.executeQuery();
        List<Portfolio> orders = new ArrayList<>();
        while(rs.next()){
            int pid = rs.getInt("TRANSACTION_ID");
            String symbol = rs.getString("symbol");
            int share = rs.getInt("share");
            double price = rs.getDouble("price");
            double amount = rs.getDouble("amount");
            String shareName = rs.getString("STOCK_NAME");
            String username = rs.getString("userid");
            LocalDate date = rs.getTimestamp("date").toLocalDateTime().toLocalDate();
            orders.add(new Portfolio(pid, accountId, symbol, share,  price, amount, shareName, date));

        }
        return orders;
    }

    public static ArrayList<Portfolio> getAllUsersPortfolios(){
        ArrayList<Portfolio> result = new ArrayList<Portfolio>();
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();

            String query = "SELECT * FROM PORTFOLIOS a join STOCKS b on a.SYMBOL=b.SYMBOL join ACCOUNTS c on a.ACCOUNTID=c.ACCOUNT_ID where a.SHARE>0";

            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {

                int pid = resultSet.getInt("portfolio_id");
                int aid = resultSet.getInt("accountid");
                String symbol = resultSet.getString("symbol");
                int share = resultSet.getInt("share");
                double avgFillPrice = resultSet.getDouble("avg_fill_price");
                double amount = resultSet.getDouble("amount");
                String shareName = resultSet.getString("STOCK_NAME");
                String username = resultSet.getString("userid");
                //Feedback feedback = new Feedback(id, name, email, subject, message);
                Portfolio portfolio = new Portfolio(pid, aid, symbol, share, avgFillPrice, amount, shareName, username);
                result.add(portfolio);
            }
        } catch (SQLException e) {
            Log4Bigbucks.getInstance().logError("Error retrieving portfolios: " + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
    /**
     * Get all accounts for the specified user
     * @param username
     * @return
     * @throws SQLException
     */
    public static Account[] getAccounts(String username) throws SQLException {
        if (username == null || username.trim().length() == 0)
            return null;

        Connection connection = getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT ACCOUNT_ID, ACCOUNT_NAME, BALANCE FROM ACCOUNTS WHERE USERID = '" + username + "' "); /* BAD - user input should always be sanitized */

        ArrayList<Account> accounts = new ArrayList<Account>(3);
        while (resultSet.next()) {
            long accountId = resultSet.getLong("ACCOUNT_ID");
            String name = resultSet.getString("ACCOUNT_NAME");
            double balance = resultSet.getDouble("BALANCE");
            Account newAccount = new Account(accountId, name, balance);
            accounts.add(newAccount);
        }

        return accounts.toArray(new Account[accounts.size()]);
    }

    /**
     * Transfer funds between specified accounts
     * @param username
     * @param creditActId
     * @param debitActId
     * @param amount
     * @return
     */
    public static String transferFunds(String username, long creditActId, long debitActId, double amount) {

        try {

            User user = getUserInfo(username);

            Connection connection = getConnection();
            Statement statement = connection.createStatement();

            Account debitAccount = Account.getAccount(debitActId);
            Account creditAccount = Account.getAccount(creditActId);

            if (debitAccount == null) {
                return "Originating account is invalid";
            }

            if (creditAccount == null)
                return "Destination account is invalid";

            java.sql.Timestamp date = new Timestamp(new java.util.Date().getTime());

            //in real life we would want to do these updates and transaction entry creation
            //as one atomic operation

            long userCC = user.getCreditCardNumber();

            /* this is the account that the payment will be made from, thus negative amount!*/
            double debitAmount = -amount;
            /* this is the account that the payment will be made to, thus positive amount!*/
            double creditAmount = amount;

            /* Credit card account balance is the amount owed, not amount owned
             * (reverse of other accounts). Therefore we have to process balances differently*/
            if (debitAccount.getAccountId() == userCC)
                debitAmount = -debitAmount;

            //create transaction record
            statement.execute("INSERT INTO TRANSACTIONS (ACCOUNTID, DATE, TYPE, AMOUNT) VALUES (" + debitAccount.getAccountId() + ",'" + date + "'," + ((debitAccount.getAccountId() == userCC) ? "'Cash Advance'" : "'Withdrawal'") + "," + debitAmount + ")," +
                    "(" + creditAccount.getAccountId() + ",'" + date + "'," + ((creditAccount.getAccountId() == userCC) ? "'Payment'" : "'Deposit'") + "," + creditAmount + ")");

            Log4Bigbucks.getInstance().logTransaction(debitAccount.getAccountId() + " - " + debitAccount.getAccountName(), creditAccount.getAccountId() + " - " + creditAccount.getAccountName(), amount);

            if (creditAccount.getAccountId() == userCC)
                creditAmount = -creditAmount;

            //add cash advance fee since the money transfer was made from the credit card
            if (debitAccount.getAccountId() == userCC) {
                statement.execute("INSERT INTO TRANSACTIONS (ACCOUNTID, DATE, TYPE, AMOUNT) VALUES (" + debitAccount.getAccountId() + ",'" + date + "','Cash Advance Fee'," + CASH_ADVANCE_FEE + ")");
                debitAmount += CASH_ADVANCE_FEE;
                Log4Bigbucks.getInstance().logTransaction(String.valueOf(userCC), "N/A", CASH_ADVANCE_FEE);
            }

            //update account balances
            statement.execute("UPDATE ACCOUNTS SET BALANCE = " + (debitAccount.getBalance() + debitAmount) + " WHERE ACCOUNT_ID = " + debitAccount.getAccountId());
            statement.execute("UPDATE ACCOUNTS SET BALANCE = " + (creditAccount.getBalance() + creditAmount) + " WHERE ACCOUNT_ID = " + creditAccount.getAccountId());

            return null;

        } catch (SQLException e) {
            return "Transaction failed. Please try again later.";
        }
    }


    public static BigDecimal getMarketPrice(String symbol) throws IOException {
        yahoofinance.Stock stock = YahooFinance.get(symbol);
        BigDecimal price = stock.getQuote().getPrice();
        return price;
    }

    public static double getLastClosePrice(String symbol) throws SQLException {
        Connection connection = getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT PRICE_CLOSE FROM HISTORICALDATA WHERE SYMBOL = '"+symbol+"'"+"ORDER BY DATE DESC");
        if (resultSet.next()) {
            return resultSet.getDouble("PRICE_CLOSE");
        }
        return -1;
    }

    public static double getHistricalClosePrice(String symbol, String date) throws SQLException {
        Connection connection = getConnection();
        Statement statement = connection.createStatement();
        symbol = symbol.toUpperCase();
        ResultSet resultSet = statement.executeQuery("SELECT PRICE_CLOSE FROM HISTORICALDATA WHERE SYMBOL = '"+symbol+"'"+"AND DATE BETWEEN timestamp ('"+date+"',"+"'00.00.00') AND timestamp('"+date+"',"+"'00.00.01')");
        if (resultSet.next()) {
            return resultSet.getDouble("PRICE_CLOSE");
        }
        return -1;
    }
    public static String orderStocks(String username,long debitActId, String orderType, String symbol, int shares, double price) throws SQLException {
        java.sql.Timestamp date = new Timestamp(new java.util.Date().getTime());
        return orderStocks( username, debitActId,  orderType,  symbol,  shares,  price,  date);
    }

    /**
     * Order stock and update portfolio and account table
     * @param username
     * @param debitActId
     * @param orderType
     * @param symbol
     * @param shares
     * @param price
     * @return
     */
    public static String orderStocks(String username,long debitActId, String orderType, String symbol, int shares, double price, Timestamp date) throws SQLException {
        try {
            symbol = symbol.toUpperCase();
            Connection connection = getConnection();
            Statement statement = connection.createStatement();

            Account debitAccount = Account.getAccount(debitActId);
            if (debitAccount == null) {
                return "Originating account is invalid";
            }

            /* this is the account that the payment will be made from, thus negative amount!*/
            long id = debitAccount.getAccountId();

            // see if the stock is in the portfolio
            String query = "SELECT AVG_FILL_PRICE, SHARE, AMOUNT FROM PORTFOLIOS WHERE (ACCOUNTID = " + id + ") AND (SYMBOL = '" + symbol + "')";
            ResultSet resultSet = statement.executeQuery(query);

            double balance = debitAccount.getBalance();
            double assetValueChg = 0;
            int shareschg = 0;
            if (Objects.equals(orderType, "buy")) {assetValueChg = shares * price;shareschg = shares;}
            else if (Objects.equals(orderType, "sell")) {assetValueChg = -shares * price;shareschg = -shares;}

            if (resultSet.next()) {
                int Holdshare = resultSet.getInt("share");
                double HoldValue = resultSet.getDouble("amount");

                if (Objects.equals(orderType, "buy")) {
                    // identify have enough balance to buy
                    if (balance < assetValueChg) {
                        return "Order failed. No sufficient balance to buy.";
                    }
                } else if (Objects.equals(orderType, "sell")) {
                    // identify if enough stocks to sell
                    if (Holdshare < shares) {
                        return "Order failed. No enough shares to sell.";
                    }
                }
                double avg_price;
                if(Holdshare + shareschg==0){avg_price=0;}
                else{avg_price = (HoldValue + assetValueChg) / (Holdshare + shareschg);}

                int postShares =  Holdshare + shareschg;
                double postAmount =  HoldValue + assetValueChg;
                statement.execute("UPDATE PORTFOLIOS SET SHARE = " + postShares + ", AMOUNT = " + postAmount+ ",AVG_FILL_PRICE=" + avg_price + "  WHERE (ACCOUNTID = " + id + ") AND (SYMBOL = '" + symbol + "')");

            } else {
                if (Objects.equals(orderType, "buy") && balance< assetValueChg){return "Order failed. No sufficient balance to buy.";}
                if (Objects.equals(orderType, "sell")){
                    return "No stock to sell";
                }
                //the ordered stock is not in the portfolio
                statement.execute("INSERT INTO PORTFOLIOS (ACCOUNTID, SYMBOL, SHARE, AMOUNT, AVG_FILL_PRICE) VALUES(" + id + ",'" + symbol + "'," + shares + "," + assetValueChg + "," + price + ")");

            }

            //Transaction records
            statement.execute("INSERT INTO TRANSACTIONS (ACCOUNTID, DATE, TYPE, SYMBOL, SHARE, PRICE, AMOUNT) VALUES (" + id + ",'" + date + "','" + orderType+"','" +symbol + "'," + shareschg + "," + price + "," + assetValueChg + ")");

            //update balance
            statement.execute("UPDATE ACCOUNTS SET BALANCE = " + (debitAccount.getBalance() - assetValueChg) + " WHERE ACCOUNT_ID = " + debitAccount.getAccountId());
            Log4Bigbucks.getInstance().logOrder(debitAccount.getAccountId()+" - "+ debitAccount.getAccountName(), orderType,symbol , shares, date, assetValueChg, price);

            return null;
//
        } catch (SQLException e) {
            return "Transaction failed. Please try again later.";
        }
    }

    /**
     * Get transaction information for the specified accounts in the date range (non-inclusive of the dates)
     * @param startDate
     * @param endDate
     * @param accounts
     * @param rowCount
     * @return
     */
    public static Transaction[] getTransactions(String startDate, String endDate, Account[] accounts, int rowCount) throws SQLException {

        if (accounts == null || accounts.length == 0)
            return null;

        Connection connection = getConnection();


        Statement statement = connection.createStatement();

        if (rowCount > 0)
            statement.setMaxRows(rowCount);

        StringBuffer acctIds = new StringBuffer();
        acctIds.append("ACCOUNTID = " + accounts[0].getAccountId());
        for (int i = 1; i < accounts.length; i++) {
            acctIds.append(" OR ACCOUNTID = " + accounts[i].getAccountId());
        }

        String dateString = null;

        if (startDate != null && startDate.length() > 0 && endDate != null && endDate.length() > 0) {
            dateString = "DATE BETWEEN '" + startDate + " 00:00:00' AND '" + endDate + " 23:59:59'";
        } else if (startDate != null && startDate.length() > 0) {
            dateString = "DATE > '" + startDate + " 00:00:00'";
        } else if (endDate != null && endDate.length() > 0) {
            dateString = "DATE < '" + endDate + " 23:59:59'";
        }

        String query = "SELECT * FROM TRANSACTIONS WHERE (" + acctIds.toString() + ") " + ((dateString == null) ? "" : "AND (" + dateString + ") ") + "ORDER BY DATE DESC";
        ResultSet resultSet = null;

        try {
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            int errorCode = e.getErrorCode();
            if (errorCode == 30000)
                throw new SQLException("Date-time query must be in the format of yyyy-mm-dd HH:mm:ss", e);

            throw e;
        }
        ArrayList<Transaction> transactions = new ArrayList<Transaction>();
        while (resultSet.next()) {
            int transId = resultSet.getInt("TRANSACTION_ID");
            long actId = resultSet.getLong("ACCOUNTID");
            Timestamp date = resultSet.getTimestamp("DATE");
            String desc = resultSet.getString("TYPE");
            double amount = resultSet.getDouble("AMOUNT");
            transactions.add(new Transaction(transId, actId, date, desc, amount));
        }

        return transactions.toArray(new Transaction[transactions.size()]);
    }

    public static String[] getBankUsernames() {

        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            //at the moment this query limits transfers to
            //transfers between two user accounts
            ResultSet resultSet = statement.executeQuery("SELECT USER_ID FROM PEOPLE");

            ArrayList<String> users = new ArrayList<String>();

            while (resultSet.next()) {
                String name = resultSet.getString("USER_ID");
                users.add(name);
            }

            return users.toArray(new String[users.size()]);
        } catch (SQLException e) {
            e.printStackTrace();
            return new String[0];
        }
    }

    public static Account getAccount(long accountNo) throws SQLException {

        Connection connection = getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT ACCOUNT_NAME, BALANCE FROM ACCOUNTS WHERE ACCOUNT_ID = " + accountNo + " "); /* BAD - user input should always be sanitized */

        ArrayList<Account> accounts = new ArrayList<Account>(3);
        while (resultSet.next()) {
            String name = resultSet.getString("ACCOUNT_NAME");
            double balance = resultSet.getDouble("BALANCE");
            Account newAccount = new Account(accountNo, name, balance);
            accounts.add(newAccount);
        }

        if (accounts.size() == 0)
            return null;

        return accounts.get(0);
    }

    public static String addAccount(String username, String acctType) {
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            statement.execute("INSERT INTO ACCOUNTS (USERID,ACCOUNT_NAME,BALANCE) VALUES ('" + username + "','" + acctType + "', 1000000)");
            return null;
        } catch (SQLException e) {
            return e.toString();
        }
    }

    public static String addSpecialUser(String username, String password, String firstname, String lastname) {
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            statement.execute("INSERT INTO SPECIAL_CUSTOMERS (USER_ID,PASSWORD,FIRST_NAME,LAST_NAME,ROLE) VALUES ('" + username + "','" + password + "', '" + firstname + "', '" + lastname + "','user')");
            return null;
        } catch (SQLException e) {
            return e.toString();

        }
    }

    public static String addUser(String username, String password, String firstname, String lastname) {
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            statement.execute("INSERT INTO PEOPLE (USER_ID,PASSWORD,FIRST_NAME,LAST_NAME,ROLE) VALUES ('" + username + "','" + password + "', '" + firstname + "', '" + lastname + "','user')");
            return null;
        } catch (SQLException e) {
            return e.toString();

        }
    }

    public static String changePassword(String username, String password) {
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            statement.execute("UPDATE PEOPLE SET PASSWORD = '" + password + "' WHERE USER_ID = '" + username + "'");
            return null;
        } catch (SQLException e) {
            return e.toString();

        }
    }


    public static long storeFeedback(String name, String email, String subject, String comments) {
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            statement.execute("INSERT INTO FEEDBACK (NAME,EMAIL,SUBJECT,COMMENTS) VALUES ('" + name + "', '" + email + "', '" + subject + "', '" + comments + "')", Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = statement.getGeneratedKeys();
            long id = -1;
            if (rs.next()) {
                id = rs.getLong(1);
            }
            return id;
        } catch (SQLException e) {
            Log4Bigbucks.getInstance().logError(e.getMessage());
            return -1;
        }
    }

    private static Timestamp converDate(Calendar cal){
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//        String format1 = format.format(cal.getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(cal.getTime());
        java.sql.Timestamp timestamp = Timestamp.valueOf(time);
        return timestamp;
    }


    public static String getHistoricalData(String stock, int year, String searchType) throws SQLException, IOException {

        Connection connection = getConnection();
        Statement statement = connection.createStatement();

        YahooStockAPI yahooStockAPI = new YahooStockAPI();
        List<HistoricalQuote> history = null;
        try {
            history = yahooStockAPI.getHistory(stock, year, searchType);
        } catch (NullPointerException e) {
            return stock + " stock history is null";
        }
        try {
            for (HistoricalQuote quote : history) {

                String symbol = quote.getSymbol();
                BigDecimal close = quote.getClose();
                BigDecimal open = quote.getOpen();
                BigDecimal high = quote.getHigh();
                BigDecimal low = quote.getLow();
                Timestamp timestamp = converDate(quote.getDate());


                String sql = "Insert into HISTORICALDATA (symbol, date, price_open, price_high, price_low, price_close) values ('" + symbol + "','" + timestamp + "'," + open + "," + high + "," + low + "," + close + ")";
                statement.executeUpdate(sql);


            }
        } catch (SQLException e) {
            return stock + " duplicate primary key";
        }
        return null;

    }

    public static String[] getStocksInDB() throws SQLException {
        Connection connection = getConnection();
        Statement statement = connection.createStatement();

        ResultSet resultSet = statement.executeQuery("SELECT DISTINCT SYMBOL FROM HISTORICALDATA");
        ArrayList<String> stocks = new ArrayList<String>();
        while(resultSet.next()){
            String stock = resultSet.getString("symbol");
            stocks.add(stock);
        }
        return stocks.toArray(new String[stocks.size()]);
    }

    public static Timestamp getStockLastDate(String symbol) throws SQLException {
        Timestamp date= null;
        Connection connection = getConnection();
        Statement statement = connection.createStatement();

        ResultSet resultSet =
                statement.executeQuery("SELECT MAX(DATE) FROM HISTORICALDATA WHERE SYMBOL = '" + symbol + "'");
        if (resultSet.next()){
            date = resultSet.getTimestamp(1);
        }
        return date;
    }


    public static String updateAllData() throws SQLException, IOException{
        Connection connection = getConnection();
        Statement statement = connection.createStatement();
        YahooStockAPI yahooStockAPI = new YahooStockAPI();
        String[] symbolsInDB = getStocksInDB();

        for (int i=0;i<symbolsInDB.length;i++){
            String st = symbolsInDB[i];
            Timestamp date = getStockLastDate(st);
            Calendar from = Calendar.getInstance();
//            from.add(Calendar.DATE, 1);
            from.setTimeInMillis(date.getTime());

            Calendar to = Calendar.getInstance();
            List<HistoricalQuote> history = null;
            try {
                history = yahooStockAPI.getHistory(st, from, to);
            } catch (NullPointerException e) {
                return st + " stock history is null";
            }

            for (HistoricalQuote quote : history) {

            String symbol = quote.getSymbol();
            BigDecimal close = quote.getClose();
            BigDecimal open = quote.getOpen();
            BigDecimal high = quote.getHigh();
            BigDecimal low = quote.getLow();
            Timestamp timestamp = converDate(quote.getDate());

                try {
                    String sql = "Insert into HISTORICALDATA (symbol, date, price_open, price_high, price_low, price_close) values ('" + symbol + "','" + timestamp + "'," + open + "," + high + "," + low + "," + close + ")";
                    statement.executeUpdate(sql);
                    System.out.println("Update "+symbol+" "+timestamp+" Data");
                } catch (SQLException e) {

                }
            }


        }


//        Calendar from = Calendar.getInstance();
//        Calendar to = Calendar.getInstance();
//        from.add(Calendar.YEAR, Integer.parseInt("-"+year));
//
//        yahoofinance.Stock stock = YahooFinance.get(stockName);

        return null;
    }

    public static void fetchAllData() throws SQLException, IOException {
        Stock[] stocks = getStocks();
        int size = stocks.length;
        for(int i=0; i<size;i++){
            String symbol = stocks[i].getStockSymbol();
            String error = getHistoricalData(symbol, 5,"daily");
            if (error==null) {
                System.out.println(i + "/" + size + " extraction completed");
            }else{
                System.out.println(i + "/" + size + " exists");
            }
        }
    }


    public static void printDB(String tableName) throws SQLException {
        Connection connection = getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * from " + tableName);
        ResultSetMetaData rsmd = resultSet.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        while (resultSet.next()) {
            for (int i = 1; i <= columnsNumber; i++) {
                if (i > 1) System.out.print(",  ");
                String columnValue = resultSet.getString(i);
                System.out.print(rsmd.getColumnName(i) + " " + columnValue);
            }
            System.out.println("");
        }
    }
    public static void initAccount(long accountId) throws SQLException {
        Connection connection = getConnection();
        Statement statement = connection.createStatement();
        statement.execute("DELETE FROM PORTFOLIOS WHERE ACCOUNTID = "+ accountId);
        statement.execute("DELETE FROM TRANSACTIONS WHERE ACCOUNTID ="+accountId);
        statement.execute("UPDATE ACCOUNTS SET BALANCE = 1000000 WHERE ACCOUNT_ID =" + accountId);
        System.out.println("Account "+accountId+" has been initialized.");
    }



    public static void main(String[] args) throws SQLException, IOException {
        initAccount(800000);
//        DBUtil db = new DBUtil();
////
//        Connection connection = db.getConnection();
//        instance.initDB();
////        db.DBstoreData("SPY",5,"daily");

    }

}


