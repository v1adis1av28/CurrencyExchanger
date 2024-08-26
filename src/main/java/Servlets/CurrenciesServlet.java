package Servlets;

import DTO.Error;
import Exceptions.CurrencyNotFoundException;
import Exceptions.DatabaseException;
import Exceptions.NotUniqueObjectException;
import Services.CurrenciesService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(name = "Currencies", value = "/currencies")
public class CurrenciesServlet extends BaseServlet {

    protected CurrenciesService service = new CurrenciesService();

    //Метод Который получает GET запрос на получение всех валют из базы данных
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = prepareResponse(response);
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(BASE_URL);
            JsonArray jsonArray = service.ProccessGetCurrenciesRequest(connection);
            out.println(jsonArray);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (CurrencyNotFoundException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            out.print(new Gson().toJson(new Error("Currencies not found")));
        } catch (DatabaseException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(new Gson().toJson(new Error("Database error")));
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print(new Gson().toJson(new Error("Database connection error")));
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = prepareResponse(response);
        String name = request.getParameter("name");
        String code = request.getParameter("code");
        String sign = request.getParameter("sign");

        if (name == null || code == null || sign == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print(new Gson().toJson(new Error("missing parametr")));
            return;
        }
        try {
            Connection connection = DriverManager.getConnection(BASE_URL);
            String gson = service.ProcessPostCurrenciesRequest(connection, name, code, sign);
            if(!gson.contains("error"))
                response.setStatus(HttpServletResponse.SC_CREATED);
            else
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.println(gson);
            out.flush();
        }
        catch (NotUniqueObjectException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\": \"" + e.getMessage() + "\"}");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}