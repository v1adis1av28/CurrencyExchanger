package utils;

import java.sql.*;
import java.util.Random;

public class utils {
    public static int GenerateID(Connection connection) throws SQLException {
        Random rand = new Random();
        while(true)
        {
            int randomID = rand.nextInt(1000);
            String sql = "select * from currencies where ID=?;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, randomID);
            ResultSet rs = ps.executeQuery();
            if(!rs.next())
            {
                return randomID;
            }
        }
    }

    //Проверяет наличие кода в базе данных
    public static boolean isCodeValid(Connection connection, String code) {
        String sql = "SELECT * FROM Currencies WHERE Code = ?;";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, code);
            ResultSet rs = ps.executeQuery(); // вызываем executeQuery без аргументов
            return rs.next(); // если есть хотя бы одна запись, возвращаем true
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static int FindMaxId(Connection connection) throws SQLException {
        String sql = "select Max(ID) from ExchangeRates";
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet resultSet = ps.executeQuery();
        if(resultSet.next())
        {
            return resultSet.getInt(1);
        }
        else
            return 1;
    }

    public static boolean CheckId(Connection connection, String id) throws SQLException {
        String sql = "select * from Currencies where Code=?;";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean CheckPair(Connection con, String baseId, String targetId) throws SQLException {
        // Исправленный SQL-запрос
        String sql = "SELECT 1 FROM ExchangeRates er " +
                "JOIN Currencies c1 ON er.BaseCurrencyId = c1.ID " +
                "JOIN Currencies c2 ON er.TargetCurrency = c2.ID " +
                "WHERE c1.Code = ? AND c2.Code = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, baseId);  // Значение для первого параметра
            ps.setString(2, targetId); // Значение для второго параметра

            try (ResultSet resultSet = ps.executeQuery()) {
                // Если результат содержит хотя бы одну строку, возвращаем true
                return resultSet.next();
            }
        } catch (SQLException e) {
            throw new SQLException(e.getMessage(), e);
        }
    }

    public static int findIdByCode(Connection connection, String code) throws SQLException {
        String sql = "SELECT ID FROM Currencies WHERE Code = ?;";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, code);
        ResultSet resultSet = ps.executeQuery();
        return resultSet.next() ? resultSet.getInt(1) : -1;
    }
}

