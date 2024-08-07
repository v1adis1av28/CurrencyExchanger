package Servlets;

import DTO.Currency;
import Services.CurrenciesService;
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
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "Currencies", value = "/currencies")
public class CurrenciesServlet extends BaseServlet {

    protected CurrenciesService service = new CurrenciesService();

    //Метод Который получает GET запрос на получение всех валют из базы данных
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(BASE_URL);
            Statement statement = connection.createStatement();
            JsonArray jsonArray = service.ProccessGetCurrenciesRequest(statement);
            out.println(jsonArray);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

    }
}
//
//GET /currencies #
//Получение списка валют. Пример ответа:
//        [
//        {
//        "id": 0,
//        "name": "United States dollar",
//        "code": "USD",
//        "sign": "$"
//        },
//        {
//        "id": 0,
//        "name": "Euro",
//        "code": "EUR",
//        "sign": "€"
//        }
//        ]
//HTTP коды ответов:
//
//Успех - 200
//Ошибка (например, база данных недоступна) - 500