package Servlets;

import Exceptions.CurrencyNotFoundException;
import Services.CurrencyService;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@WebServlet (name="currency", value="/currency/*")
public class CurrencyServlet extends BaseServlet {

    private CurrencyService currencyService = new CurrencyService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = prepareResponse(resp);
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || !pathInfo.matches("/[A-Z]{3}")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println(new Gson().toJson(new Error("Invalid currency code format")));
            return;
        }

        String code = pathInfo.substring(1); // Извлечение кода валюты из URL

        try (Connection con = DriverManager.getConnection(BASE_URL)) {
            String json = currencyService.FindCurrency(con, code);
            resp.setStatus(HttpServletResponse.SC_OK);
            out.println(json);
        } catch (CurrencyNotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            out.println(new Gson().toJson(new Error("Currency not found")));
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.println(new Gson().toJson(new Error("Database error")));
        } finally {
            out.close();
        }
    }


}
