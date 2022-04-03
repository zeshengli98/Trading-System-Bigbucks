package com.ibm.security.appscan;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DecimalFormat;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

public class Log4Bigbucks {

    private static Log4Bigbucks _theInstance =  new Log4Bigbucks();;
    private static String logFileLocation = null;
    private Logger logger = null;

    public static Log4Bigbucks getInstance(){
        return _theInstance;
    }

    private Log4Bigbucks(){
        logger = Logger.getRootLogger();
        logger.setLevel(Level.INFO);
        PatternLayout layout = new PatternLayout("%d{yyyy-MM-dd} %d{HH:mm:ss} %p %m%n");
        try {
            logFileLocation = System.getProperty("user.home")+"/bigbucks/BigbucksLog.log";
            RollingFileAppender appender = new RollingFileAppender(layout, logFileLocation);
            appender.setMaxFileSize("100KB");
            appender.setMaxBackupIndex(1);
            logger.addAppender(appender);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void SignupError(String error){logger.error(error);}

    public void logError(String error){
        logger.error(error);
    }

    public void logInfo(String message){
        logger.info(message);
    }

    public String getLogFileLocation(){
        return logFileLocation;
    }

    public void logTransaction(String fromAccount, String toAccount, double amount){
        String format = (amount<1)?"$0.00":"$.00";
        String stringAmt = new DecimalFormat(format).format(amount);

        logger.info("Transaction >>> From: " + fromAccount + " >>> To: " + toAccount + " >>> Amount: " + stringAmt);
    }
    public void logOrder(String account, String ordertype, String stock, int share, Timestamp date, double amount, double price) {
        if (ordertype == "buy") {
            logger.info("Order Successfully >>> From: " + account + " >>> " + ordertype + " " + share +
                    " shares, market, " + stock + ", " + date.toString() + " debit cash " + amount + ", credit " +stock+" "+amount);
        } else {
            logger.info("Order Successfully >>> From: " + account + " >>> " + ordertype + " " + share +
                    " shares, market, " + stock + ", " + date.toString() + " debit "+ stock + " " + amount + ", credit cash " +amount);
        }
    }

}