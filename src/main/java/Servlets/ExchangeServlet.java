package Servlets;

import DTO.Error;
import Services.ExchangeService;
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

@WebServlet (name="Exchange", value="/exchange")
public class ExchangeServlet extends BaseServlet {
    private ExchangeService service = new ExchangeService();
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String from = request.getParameter("from");
        String to = request.getParameter("to");
        String amount = request.getParameter("amount");
        if(from == null || to == null || amount == null)
        {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.println(new Gson().toJson(new Error("400")));
            return;
        }
        try {
            Connection con = DriverManager.getConnection(BASE_URL);
            String answer = service.ProccesExchange(con,from,to,amount);
            out.println(answer);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
