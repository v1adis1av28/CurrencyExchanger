package DAO;

import DTO.ExchangeAmount;
import DTO.ExchangeRate;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ExchangeDAO {
    public static ExchangeAmount createExchangeRate(int baseId, int targetId, double exchangeRate, double amount, double convertedAmount) {
        ExchangeAmount exchange = new ExchangeAmount();
        exchange.setBaseCurrencyId(baseId);
        exchange.setTargetCurrencyId(targetId);
        exchange.setExchangeRate(new BigDecimal(exchangeRate).setScale(2,BigDecimal.ROUND_HALF_EVEN));
        exchange.setAmount((int)amount);
        exchange.setConvertedAmount(new BigDecimal(convertedAmount).setScale(2,BigDecimal.ROUND_HALF_EVEN));
        return exchange;
    }

    public static double haveExchangeRate(Connection connection, int baseId, int targetId) {
        String query = "Select rate from ExchangeRates where BaseCurrencyId = ? and TargetCurrency = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, baseId);
            statement.setInt(2,targetId);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()) {
                return resultSet.getDouble("rate");
            }
            return -1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static int FindId(Connection con, String Currency) throws SQLException {
        String query = "SELECT id FROM Currencies WHERE Code = ?";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, Currency);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }
        throw new SQLException("Валюта не найдена: " + Currency);
    }

    public static double getRate(Connection connection, int baseId, int targetId) throws SQLException {
        String query = "SELECT Rate FROM ExchangeRates WHERE BaseCurrencyId = ? AND TargetCurrency = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, baseId);
        preparedStatement.setInt(2,targetId);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.getDouble("Rate");
    }
}
