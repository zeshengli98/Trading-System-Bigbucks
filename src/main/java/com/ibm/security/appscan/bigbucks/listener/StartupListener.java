package com.ibm.security.appscan.bigbucks.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.ibm.security.appscan.Log4Bigbucks;
import com.ibm.security.appscan.bigbucks.util.DBUtil;
import com.ibm.security.appscan.bigbucks.util.ServletUtil;

public class StartupListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try {
		ServletUtil.initializeAppProperties(sce.getServletContext());
		ServletUtil.initializeLogFile(sce.getServletContext());
		DBUtil.isValidUser("bogus", "user");
		ServletUtil.initializeRestAPI(sce.getServletContext());
		System.out.println("Bigbucks initialized");
		} catch (Exception e) {
			Log4Bigbucks.getInstance().logError("Error during Bigbucks initialization:" + e.getMessage());
//			e.printStackTrace();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// do nothing
	}

}
