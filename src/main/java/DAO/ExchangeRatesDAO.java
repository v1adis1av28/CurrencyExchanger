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
        String sql = "SELECT er.ID, er.BaseCurrencyId, er.TargetCurrency, er.Rate, bc.Code AS BaseCurrencyCode, tc.Code AS TargetCurrencyCode " +
                "FROM ExchangeRates er " +
                "JOIN Currencies bc ON er.BaseCurrencyId = bc.ID " +
                "JOIN Currencies tc ON er.TargetCurrency = tc.ID";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet resultSet = ps.executeQuery()) {

            while (resultSet.next()) {
                ExchangeRate er = new ExchangeRate();
                er.setID(resultSet.getInt("ID"));
                er.setRate(resultSet.getDouble("Rate"));
                er.setBaseCurrencyId(resultSet.getInt("BaseCurrencyId"));
                er.setTargetCurrencyId(resultSet.getInt("TargetCurrency"));
                er.setBaseCurrencyCode(resultSet.getString("BaseCurrencyCode"));
                er.setTargetCurrencyCode(resultSet.getString("TargetCurrencyCode"));
                exchangeRates.add(er);
            }
        }
        return exchangeRates;
    }


    public static void InsertExchangeRate(Connection con, ExchangeRate exchangeRate) throws SQLException {
        String sql = "INSERT INTO ExchangeRates(BaseCurrencyId, TargetCurrency, Rate) VALUES(?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, exchangeRate.getBaseCurrencyId());
            ps.setInt(2, exchangeRate.getTargetCurrencyId());
            ps.setDouble(3, exchangeRate.getRate());
            ps.executeUpdate();

            // Получаем сгенерированный ID и сохраняем его в объекте
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    exchangeRate.setID(generatedKeys.getInt(1));
                }
            }
        }
    }

}
