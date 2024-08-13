package Servlets;

import DTO.Error;
import DTO.ExchangeRate;
import Services.ExchangeRatesService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
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

@WebServlet(name="ExchangeRates", value="/exchangeRates")
public class ExchangeRatesServlet extends BaseServlet{
    private ExchangeRatesService service = new ExchangeRatesService();
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = prepareResponse(response);
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(BASE_URL);
            JsonArray answer = service.ProccesGetExchangeRates(connection);
            out.println(answer.toString());
            if(answer.toString().contains("error"))
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            else
                response.setStatus(HttpServletResponse.SC_OK);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = prepareResponse(response);
        Connection connection = null;
        int BaseCurrencyId = Integer.parseInt(request.getParameter("BaseCurrencyId"));
        int TargetCurrencyId = Integer.parseInt(request.getParameter("TargetCurrency"));
        double Rate = Double.parseDouble(request.getParameter("Rate"));
        if(request.getParameter("BaseCurrencyId").isEmpty() ||
        request.getParameter("TargetCurrency").isEmpty()||
        request.getParameter("Rate").isEmpty())
        {
            out.println(new Gson().toJson(new Error("400")));//TODOO - пофиксить чтобы правильно обрабатывал отсутсвие параметров в body
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        try {
            connection = DriverManager.getConnection(BASE_URL);
            out.println(service.ProccesPostExchangeRates(connection, BaseCurrencyId, TargetCurrencyId, Rate));
            response.setStatus(HttpServletResponse.SC_CREATED);
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new RuntimeException(e);
        }
    }


}