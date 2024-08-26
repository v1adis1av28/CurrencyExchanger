package Servlets;

import DTO.Error;
import Exceptions.CurrencyNotFoundException;
import Exceptions.CurrencyPairExistsException;
import Exceptions.NotUniqueObjectException;
import Services.ExchangeRatesService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@WebServlet(name = "ExchangeRates", value = "/exchangeRates")
public class ExchangeRatesServlet extends BaseServlet {
    private ExchangeRatesService service = new ExchangeRatesService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = prepareResponse(response);
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(BASE_URL);
            JsonArray answer = service.ProccesGetExchangeRates(connection);
            out.println(answer.toString());
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.println(new Gson().toJson(new Error("500")));
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = prepareResponse(response);
        Connection connection = null;

        try {
            PostData postData = parsePostData(request);

            if (postData == null || postData.baseCurrencyCode.length() <= 0 || postData.targetCurrencyCode.length() <= 0 || postData.rate <= 0) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.println(new Gson().toJson(new Error("400")));
                return;
            }
            connection = DriverManager.getConnection(BASE_URL);
            String result = service.ProccesPostExchangeRates(connection, postData.baseCurrencyCode, postData.targetCurrencyCode, postData.rate);
            out.println(result);
            response.setStatus(HttpServletResponse.SC_CREATED);
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.println(new Gson().toJson(new Error("500")));
            e.printStackTrace();
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println(new Gson().toJson(new Error("400")));
            e.printStackTrace();
        } catch (CurrencyNotFoundException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            out.println(new Gson().toJson(new Error("404")));
            e.printStackTrace();
        } catch (CurrencyPairExistsException e) {
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            out.println(new Gson().toJson(new Error("409")));
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private PostData parsePostData(HttpServletRequest request) throws IOException {
        // Получаем параметры из запроса
        String baseCurrencyIdParam = request.getParameter("baseCurrencyCode");
        String targetCurrencyIdParam = request.getParameter("targetCurrencyCode");
        String rateParam = request.getParameter("rate");

        if (baseCurrencyIdParam == null || targetCurrencyIdParam == null || rateParam == null ||
                baseCurrencyIdParam.isEmpty() || targetCurrencyIdParam.isEmpty() || rateParam.isEmpty()) {
            BufferedReader reader = request.getReader();
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String body = sb.toString();
            String[] params = body.split("&");
            for (String param : params) {
                String[] keyValue = param.split("=");
                if (keyValue.length == 2) {
                    switch (keyValue[0]) {
                        case "BaseCurrencyId":
                            baseCurrencyIdParam = keyValue[1];
                            break;
                        case "TargetCurrency":
                            targetCurrencyIdParam = keyValue[1];
                            break;
                        case "Rate":
                            rateParam = keyValue[1];
                            break;
                    }
                }
            }
        }
        try {
            double rate = Double.parseDouble(rateParam);
            return new PostData(baseCurrencyIdParam, targetCurrencyIdParam, rate);
        } catch (NumberFormatException e) {
            return null;
        }
    }


    // Класс для хранения данных POST-запроса
    private static class PostData {
        String baseCurrencyCode;
        String targetCurrencyCode;
        double rate;

        PostData(String baseCurrencyId, String targetCurrencyId, double rate) {
            this.baseCurrencyCode = baseCurrencyId;
            this.targetCurrencyCode = targetCurrencyId;
            this.rate = rate;
        }
    }
}
