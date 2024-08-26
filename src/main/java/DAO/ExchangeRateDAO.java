package DAO;

import DTO.Error;
import DTO.ExchangeRate;
import Exceptions.DAOException;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ExchangeRateDAO {

    public static ExchangeRate getExchangeRate(Connection con, String firstCode, String secondCode) throws SQLException, DAOException {
        ExchangeRate exchangeRate = new ExchangeRate();
        String SQL = "SELECT  ID, BaseCurrencyId, TargetCurrency, Rate  FROM  ExchangeRates WHERE BaseCurrencyId = (select ID from currencies where Code = ?) AND TargetCurrency = (select ID from currencies where Code = ?);";
        try (PreparedStatement ps = con.prepareStatement(SQL)) {
            ps.setString(1, firstCode);
            ps.setString(2, secondCode);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new ExchangeRate(rs.getInt("ID"), rs.getInt("BaseCurrencyId"), rs.getInt("TargetCurrency"), rs.getDouble("Rate"));
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error fetching exchange rate", e);
        }
    }

    public static ExchangeRate patchExchangeRate(Connection con,double newRate, String firstCode, String secondCode) throws SQLException {
        String updateSQL = "UPDATE ExchangeRates SET Rate = ? WHERE BaseCurrencyId = (SELECT id FROM Currencies WHERE Code = ?) AND TargetCurrency = (SELECT Id FROM Currencies WHERE code = ?);";
        try {
            PreparedStatement ps = con.prepareStatement(updateSQL);
            ps.setDouble(1, newRate);
            ps.setString(2, firstCode);
            ps.setString(3, secondCode);
            ps.executeUpdate();
            return getUpdatedRate(con, newRate, firstCode, secondCode);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isPair(Connection con, String firstCode, String secondCode) throws SQLException {
        String SQL = "SELECT  *  FROM  ExchangeRates WHERE BaseCurrencyId = (select ID from currencies where Code = ? )" +
                " AND TargetCurrency = (select ID from currencies where Code = ?);";
        try {
            PreparedStatement ps = con.prepareStatement(SQL);
            ps.setString(1, firstCode);
            ps.setString(2, secondCode);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return true;
            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
        private static ExchangeRate getUpdatedRate(Connection con, double newRate, String firstCode, String secondCode) throws SQLException {
            String sql = "Select ID, BaseCurrencyId, TargetCurrency, Rate from ExchangeRates where rate = ? " +
                    "and BaseCurrencyId = (select ID from currencies where Code = ?) and TargetCurrency = (select ID from currencies where Code = ?);";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setDouble(1, newRate);
            ps.setString(2, firstCode);
            ps.setString(3, secondCode);
            ResultSet rs = ps.executeQuery();
            ExchangeRate exchangeRate = new ExchangeRate(rs.getInt("ID"), rs.getInt("BaseCurrencyId"), rs.getInt("TargetCurrency"), rs.getDouble("Rate"));
            return exchangeRate;
        }

}

