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

    public static boolean CheckId(Connection connection, int id) throws SQLException {
        String sql = "select * from Currencies where ID=?;";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean CheckPair(Connection con, int baseId, int targetId) throws SQLException {
        String sql = "select * from ExchangeRates WHERE BaseCurrencyId=? AND TargetCurrency=?;";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, baseId);
            ps.setInt(2, targetId);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                return true;
            }
            return false;
        }
        catch (SQLException e) {
            throw new SQLException(e.getMessage());
        }
    }

}
