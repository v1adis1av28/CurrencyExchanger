package Servlets;

import DTO.Error;
import Services.ExchangeService;
import Exceptions.CurrencyNotFoundException;
import Exceptions.InvalidAmountException;
import Exceptions.ExchangeRateNotFoundException;
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

@WebServlet(name = "Exchange", value = "/exchange")
public class ExchangeServlet extends BaseServlet {
    private ExchangeService service = new ExchangeService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = prepareResponse(response);
        String from = request.getParameter("from");
        String to = request.getParameter("to");
        String amount = request.getParameter("amount");

        if (from == null || to == null || amount == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println(new Gson().toJson(new Error("400")));
            return;
        }

        try (Connection con = DriverManager.getConnection(BASE_URL)) {
            String answer = service.ProccesExchange(con, from, to, amount);
            out.println(answer);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.println(new Gson().toJson(new Error("500")));
            e.printStackTrace();
        } catch (CurrencyNotFoundException | InvalidAmountException e) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            out.println(new Gson().toJson(new Error("404")));
            e.printStackTrace();
        }
    }
}
