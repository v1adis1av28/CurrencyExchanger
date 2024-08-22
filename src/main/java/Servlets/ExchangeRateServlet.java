package Servlets;

import DTO.Error;
import Services.ExchangeRateService;
import com.google.gson.Gson;
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
import java.util.Enumeration;

@WebServlet(name = "ExchangeRate", value = "/exchangeRate/*")
public class ExchangeRateServlet extends BaseServlet {
    private ExchangeRateService service = new ExchangeRateService();
    private int MAX_REQUEST_INFO_LENGTH = 6;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = prepareResponse(response);

        if (!isRequestValid(request)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println(new Gson().toJson(new Error("400")));
            return;
        }

        try (Connection conn = DriverManager.getConnection(BASE_URL)) {
            String answer = service.ProccesGetExchangeRate(conn, request.getPathInfo().substring(1));
            if (!answer.contains("error")) {
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
            out.println(answer);
            out.flush();
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.println(new Gson().toJson(new Error("500")));
            out.flush();
        }
    }

    protected void doPatch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        // Попытка получить параметр rate из x-www-form-urlencoded данных стандартным методом
        String rate = request.getParameter("rate");

        // Если стандартный способ не сработал, выполняем ручной разбор тела запроса
        if (rate == null || rate.isEmpty()) {
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
                if (keyValue[0].equals("rate")) {
                    rate = keyValue[1];
                    break;
                }
            }
        }

        // Проверка, был ли найден параметр rate
        if (rate == null || rate.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println(new Gson().toJson(new Error("400")));
            out.flush();
            return;
        }

        try (Connection connection = DriverManager.getConnection(BASE_URL)) {
            double newRate = Double.parseDouble(rate);

            // Обновление курса в базе данных
            String currencyPair = request.getPathInfo().substring(1);
            String updatedRate = service.ProccesPatchExchangeRate(connection, newRate, currencyPair);

            if (!updatedRate.contains("error")) {
                response.setStatus(HttpServletResponse.SC_OK);
                out.println(updatedRate);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.println(new Gson().toJson(new Error("404")));
            }
            out.flush();
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.println(new Gson().toJson(new Error("500")));
            out.flush();
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println(new Gson().toJson(new Error("400")));
            out.flush();
        }
    }

    private boolean isRequestValid(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String requestInfo = request.getPathInfo().substring(1);
        return requestURI.contains("/exchangeRate") && requestInfo.length() == MAX_REQUEST_INFO_LENGTH;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getMethod().equalsIgnoreCase("PATCH")) {
            doPatch(req, resp);
        } else {
            super.service(req, resp);
        }
    }
}
