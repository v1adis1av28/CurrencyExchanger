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

@WebServlet(name = "Currencies", value = "/currencies")
public class CurrenciesServlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
        super.init();
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("utf-8");
        PrintWriter out = resp.getWriter();
        String sql = "select * from currencies";
        ArrayList<Currency> currencies = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:C:/sqlite/CurrencyProject.db");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while(resultSet.next()) {
                Currency currency = new Currency();
                currency.setID(resultSet.getInt("ID"));
                currency.setCode(resultSet.getString("Code"));
                currency.setFullName(resultSet.getString("FullName"));
                currency.setSign(resultSet.getString("Sign"));
                currencies.add(currency);
            }
            for (Currency currency : currencies) {
                System.out.println(new Gson().toJson(currency));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
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