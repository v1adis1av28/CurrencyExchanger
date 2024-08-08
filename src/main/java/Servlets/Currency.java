package Servlets;

import Services.CurrencyService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@WebServlet (name="currency", value="/currency/*")
public class Currency extends BaseServlet {

    private CurrencyService currencyService = new CurrencyService();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String pathInfo = req.getPathInfo();
        if(!req.getRequestURI().contains("/currency/"))//Обрабатываем случай,неправильно набран запрос
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        pathInfo = pathInfo.substring(pathInfo.length()-3);//Выделяем информацию о запросе конкретной валюты
        try {
            Connection con = DriverManager.getConnection(BASE_URL);
            Statement stmt = con.createStatement();
            //Передаем в ф-ю сервиса pathInfo+stmnt
            String json = currencyService.FindCurrency(stmt,pathInfo);
            resp.setStatus(HttpServletResponse.SC_OK);
            out.println(json);
            out.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
