package DAO;

import Exceptions.CurrencyNotFoundException;
import Exceptions.ExchangeRateNotFoundException;
import DTO.ExchangeAmount;
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
        exchange.setExchangeRate(new BigDecimal(exchangeRate).setScale(2, BigDecimal.ROUND_HALF_EVEN));
        exchange.setAmount((int) amount);
        exchange.setConvertedAmount(new BigDecimal(convertedAmount).setScale(2, BigDecimal.ROUND_HALF_EVEN));
        return exchange;
    }

    public static double haveExchangeRate(Connection connection, int baseId, int targetId) throws SQLException, ExchangeRateNotFoundException {
        String query = "SELECT rate FROM ExchangeRates WHERE BaseCurrencyId = ? AND TargetCurrency = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, baseId);
            statement.setInt(2, targetId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble("rate");
                }
            }
        }
        return -1;
    }

    public static int FindId(Connection con, String Currency) throws SQLException, CurrencyNotFoundException {
        String query = "SELECT id FROM Currencies WHERE Code = ?";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, Currency);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
                throw new CurrencyNotFoundException("Currency not found: " + Currency);
            }
        }
    }

    public static double getRate(Connection connection, int baseId, int targetId) throws SQLException, ExchangeRateNotFoundException {
        String query = "SELECT Rate FROM ExchangeRates WHERE BaseCurrencyId = ? AND TargetCurrency = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, baseId);
            preparedStatement.setInt(2, targetId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble("Rate");
                }
            }
        }
        return -1;
    }
}
