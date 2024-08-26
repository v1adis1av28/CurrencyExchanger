package Servlets;

import DTO.Currency;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
public class BaseServlet extends HttpServlet {
    protected String BASE_URL;
    @Override
    public void init() throws ServletException {
        super.init();
        try {
            Class.forName("org.sqlite.JDBC");

            // Получаем реальный путь до файла базы данных в папке ресурсов
            String relativePath = getServletContext().getRealPath("/WEB-INF/classes/CurrencyProject.db");

            System.out.println("Database path: " + relativePath);  // Для проверки пути
            BASE_URL = "jdbc:sqlite:" + relativePath;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void destroy() {
        super.destroy();
    }

    protected PrintWriter prepareResponse(HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        return response.getWriter();
    }
}