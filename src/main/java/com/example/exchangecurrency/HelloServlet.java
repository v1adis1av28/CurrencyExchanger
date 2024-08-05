package com.example.exchangecurrency;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

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
        resp.setContentType("text/html");
        try (PrintWriter out = resp.getWriter()) {
            out.println("<html><body>");
            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:C:/sqlite/CurrencyProject.db")) {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT  *  FROM Currencies");
                while (rs.next()) {
                    out.println("<h1>" + rs.getInt("ID") + rs.getString("Code") +
                            rs.getString("FullName") + rs.getString("Sign") + "</h1>");
                    out.println();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            out.println("</body></html>");
        }
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
