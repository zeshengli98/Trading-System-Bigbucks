package com.ibm.security.appscan.bigbucks.servlet;

import com.ibm.security.appscan.bigbucks.model.Portfolio;
import com.ibm.security.appscan.bigbucks.util.ServletUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static com.ibm.security.appscan.bigbucks.util.DBUtil.getCurrentOrder;

@WebServlet(name = "ManagerViewCurrentOrderServlet", value = "/ManagerViewCurrentOrderServlet")
public class ManagerViewCurrentOrderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(!ServletUtil.isLoggedin(request)){
            response.sendRedirect("index.jsp");
        }

        try {
            LocalDate date = LocalDate.now();
            List<Portfolio> currOrder = getCurrentOrder();
            request.setAttribute("currOrder", currOrder);

            request.setAttribute("heading", date + " market orders");
            RequestDispatcher rd = request.getRequestDispatcher("ManagerViewCurrentOrder.jsp");
            rd.forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
