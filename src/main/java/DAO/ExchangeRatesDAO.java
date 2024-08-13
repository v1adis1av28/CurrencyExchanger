package DAO;

import DTO.Error;
import DTO.ExchangeRate;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRatesDAO {

    public static List<ExchangeRate> getExchangeRates(Connection con) throws SQLException {
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        String sql = "select * from ExchangeRates";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                DTO.ExchangeRate er = new DTO.ExchangeRate();
                er.setID(resultSet.getInt("ID"));
                er.setBaseCurrencyId(resultSet.getInt("BaseCurrencyId"));
                er.setTargetCurrencyId(resultSet.getInt("TargetCurrency"));
                er.setRate(resultSet.getDouble("Rate"));
                exchangeRates.add(er);
            }
            return exchangeRates;
        }
        catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    public static void InsertExchangeRate(Connection con, ExchangeRate exchangeRate) throws SQLException {
        String sql = "Insert into ExchangeRates(ID,BaseCurrencyId,TargetCurrency,Rate) values(?,?,?,?);";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, exchangeRate.getID());
        ps.setInt(2, exchangeRate.getBaseCurrencyId());
        ps.setInt(3, exchangeRate.getTargetCurrencyId());
        ps.setDouble(4, exchangeRate.getRate());
        ps.executeUpdate();
    }
}
