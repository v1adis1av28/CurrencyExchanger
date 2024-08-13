package Services;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import DAO.CurrencyDAO;
import DTO.Currency;
import com.google.gson.Gson;
import DTO.Error;

public class CurrencyService extends ServiceEntity {
    public CurrencyService() {}
    private Gson gson = new Gson();

    public String FindCurrency(Connection connection, String pathInfo) throws SQLException {
        Currency currency = CurrencyDAO.GetCurrency(connection, pathInfo);
        if(!isValidCode(connection,pathInfo))
        {
            return gson.toJson(new Error("404"));
        }
        return gson.toJson(currency);
    }

    private boolean isValidCode(Connection connection, String pathInfo) throws SQLException {
        return CurrencyDAO.isValid(connection, pathInfo);
    }
}