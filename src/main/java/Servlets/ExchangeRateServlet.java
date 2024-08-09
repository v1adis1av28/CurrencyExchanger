package Servlets;

import DTO.Error;
import Services.ExchangeRateService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
import java.sql.Statement;

@WebServlet(name = "ExchangeRate", value = "/exchangeRate/*")
public class ExchangeRateServlet extends BaseServlet {
    private ExchangeRateService service = new ExchangeRateService();
    private int MAX_REQUEST_INFO_LENGTH = 6;
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (!isRequestValid(request)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println(new Gson().toJson(new Error("400")));
            return;
        }

        try (Connection conn = DriverManager.getConnection(BASE_URL);
             Statement stmt = conn.createStatement()) {
            String answer = service.ProccesGetExchangeRate(stmt, request.getPathInfo().substring(1));
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
        response.setCharacterEncoding("UTF-8");
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        }
        String requestBody = stringBuilder.toString();
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(requestBody, JsonObject.class);
        if (!jsonObject.has("Rate") || !jsonObject.get("Rate").isJsonPrimitive()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String rate = jsonObject.get("Rate").getAsString();
        if (rate == null || rate.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        PrintWriter out = response.getWriter();
        try (Connection connection = DriverManager.getConnection(BASE_URL);
             Statement statement = connection.createStatement()) {
            double newRate = Double.parseDouble(rate);
            String newRateValue = service.ProccesPatchExchangeRate(statement, newRate, request.getPathInfo().substring(1));
            if (!newRateValue.contains("error")) {
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Exchange rate not found.");
            }
            out.println(newRateValue);
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
        if (!requestURI.contains("/exchangeRate") || requestInfo.length() != MAX_REQUEST_INFO_LENGTH) {
            return false;
        }
        return true;
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
