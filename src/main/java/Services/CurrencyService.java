package Services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import DTO.Currency;
import com.google.gson.Gson;
import DTO.Error;

public class CurrencyService extends ServiceEntity {
    public CurrencyService() {}

    private Gson gson = new Gson();

    public String FindCurrency(Statement statement, String pathInfo) throws SQLException {
        String sql = "select * from currencies where Code = '" + pathInfo + "';";
        if (!isValidCode(sql, statement))
            return gson.toJson(new Error("500"));
        ResultSet resultSet = statement.executeQuery(sql);
        DTO.Currency currency = new DTO.Currency();
        currency.setID(resultSet.getInt("ID"));
        currency.setCode(resultSet.getString("Code"));
        currency.setFullName(resultSet.getString("FullName"));
        currency.setSign(resultSet.getString("Sign"));
        return gson.toJson(currency);
    }

    private boolean isValidCode(String sql, Statement statement) throws SQLException {
        ResultSet resultSet = statement.executeQuery(sql);
        return resultSet.next();
    }
}