package Services;

import DTO.Error;
import DTO.ExchangeRate;
import com.google.gson.Gson;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ExchangeRateService extends ServiceEntity{
    public ExchangeRateService() {}

    public String ProccesGetExchangeRate(Statement statement, String requestInfo) {
        String firstCode = requestInfo.substring(0,3);
        String secondCode = requestInfo.substring(3,requestInfo.length());
        //Сделать проверку на наличие кодов в базе данных, если нет их ->404
        if(!isCodeValid(statement,firstCode) || !isCodeValid(statement,secondCode))
        {
            return new Gson().toJson(new Error("404"));
        }
        String SQL = "SELECT  *  FROM  ExchangeRates WHERE BaseCurrencyId = (select ID from currencies where Code = '" + firstCode
                + "') AND TargetCurrency = (select ID from currencies where Code = '" + secondCode + "');";

        try {
            ResultSet rs = statement.executeQuery(SQL);
            if(rs.next())
            {
                ExchangeRate exchangeRate = new ExchangeRate(rs.getInt("ID"),
                        rs.getInt("BaseCurrencyId"), rs.getInt("TargetCurrency"),
                        rs.getDouble("Rate"));
                return new Gson().toJson(exchangeRate);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new Gson().toJson(new Error("500"));
    }


    public String ProccesPatchExchangeRate(Statement statement, double newRate, String requestInfo) {
        String firstCode = requestInfo.substring(0,3);
        String secondCode = requestInfo.substring(3,requestInfo.length());
        if(!isCodeValid(statement,firstCode) || !isCodeValid(statement,secondCode) || !CheckPair(statement,firstCode,secondCode))
        {
            return new Gson().toJson(new Error("404"));
        }
        String updateSQL = "UPDATE ExchangeRates SET Rate = '"+ newRate +"' WHERE BaseCurrencyId = (SELECT id FROM Currencies WHERE Code = '"+firstCode+"') AND TargetCurrency = (SELECT Id FROM Currencies WHERE code = '" + secondCode + "');";
        try {
            statement.executeUpdate(updateSQL);
            ResultSet resultSet = statement.executeQuery("SELECT  *  FROM  ExchangeRates WHERE BaseCurrencyId = (select ID from currencies where Code = '" + firstCode
                    + "') AND TargetCurrency = (select ID from currencies where Code = '" + secondCode + "');");
            resultSet.next();
            ExchangeRate exchangeRate = new ExchangeRate(resultSet.getInt("id"),resultSet.getInt("BaseCurrencyId"),
                    resultSet.getInt("TargetCurrency"),newRate);
            return new Gson().toJson(exchangeRate);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean CheckPair(Statement statement, String firstCode, String secondCode) {
        String SQL = "SELECT  *  FROM  ExchangeRates WHERE BaseCurrencyId = (select ID from currencies where Code = '" + firstCode
                + "') AND TargetCurrency = (select ID from currencies where Code = '" + secondCode + "');";
        try {
            ResultSet rs = statement.executeQuery(SQL);
            if (rs.next())
                return true;
            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

        //Проверяет наличие кода в базе данных
    private boolean isCodeValid(Statement statement ,String code) {
        String sql = "select  *  from Currencies where Code= '" + code + "';";
        try {
            ResultSet rs = statement.executeQuery(sql);
            if (rs.next())
                return true;
            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
