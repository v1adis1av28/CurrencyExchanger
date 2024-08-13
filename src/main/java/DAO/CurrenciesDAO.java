package DAO;
import DTO.Currency;
import DTO.Error;
import com.google.gson.Gson;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import static utils.utils.GenerateID;

public class CurrenciesDAO {

    public static List<Currency> GetAllCurrencies(Connection con) throws SQLException {
        String sql = "select  ID,CODE,FullName, Sign  from currencies";
        try {
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            List<Currency> currencies = new ArrayList<>();
            while (resultSet.next()) {
                DTO.Currency currency = new DTO.Currency();
                currency.setID(resultSet.getInt("ID"));
                currency.setCode(resultSet.getString("Code"));
                currency.setFullName(resultSet.getString("FullName"));
                currency.setSign(resultSet.getString("Sign"));
                currencies.add(currency);
            }
            return currencies;
        } catch (SQLException e) {
            //Выкидывается при 500 ошибке
            throw new SQLException(e);
        }
    }

    public static Currency AddNewCurrency(Connection con, String name, String code, String sign) throws SQLException {
        Gson gson = new Gson();//Объект для передачи в json сервлета
        Currency currency = new Currency();
        int ID = GenerateID(con);
        String sql = "insert into currencies values (?,?,?,?);";
        PreparedStatement statement = con.prepareStatement(sql);
        statement.setInt(1, ID);
        statement.setString(2, code);
        statement.setString(3, name);
        statement.setString(4, sign);
        statement.execute();
        currency.setID(ID);
        currency.setCode(code);
        currency.setFullName(name);
        currency.setSign(sign);
        return currency;
    }
}
