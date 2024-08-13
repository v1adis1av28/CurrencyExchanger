package DAO;

import DTO.Currency;
import DTO.Error;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CurrencyDAO {

    public static Currency GetCurrency(Connection connection, String info) throws SQLException {
        String sql = "select * from currencies where Code = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, info);
        ResultSet resultSet = preparedStatement.executeQuery();
        DTO.Currency currency = new DTO.Currency();
        currency.setID(resultSet.getInt("ID"));
        currency.setCode(resultSet.getString("Code"));
        currency.setFullName(resultSet.getString("FullName"));
        currency.setSign(resultSet.getString("Sign"));
        return currency;
    }

    public static boolean isValid(Connection connection, String info) throws SQLException {
        String sql = "select * from currencies where Code = ?;";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, info);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next();
    }
}
