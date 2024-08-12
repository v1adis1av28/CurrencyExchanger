package Services;

import DTO.Error;
import DTO.ExchangeAmount;
import DTO.ExchangeRate;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ExchangeService extends ServiceEntity {
    public ExchangeService() {}

    //Логика обмена
    // -Сперва идет проверка прямого курса то есть -> Прмер. Рубли в Тенге -> если есть в ExchangeRates то просто забираем rate
    // -Если нет, прямого проверяем наличие -> Тенге в рубли -> если есть получаем рейт и отсчитываем кол-во для обмена деленное на обратный курс
    // -Если нет ни прямого, ни обратного -> Переводим рубль и тенге в доллар-> значение тенге(usd)/значение рубль(usd) за одну единицу ->получаем rate

    private String USD_CODE = "USD";

    public String ProccesExchange(Connection connection, String baseCurrency, String targetCurrency, String amount) throws SQLException {
        double exchangeRate = 0.0;
        double convertedAmount = 0.0;
        double Amount;
        try {
            Amount = Double.parseDouble(amount);
        } catch (NumberFormatException e) {
            return new Gson().toJson(new Error("Недопустимое значение amount"));
        }
        // Проверка на прямой и обратный рейт
        int baseId = FindId(connection,baseCurrency);
        int targetId = FindId(connection,targetCurrency);
        if(haveExchangeRate(connection,baseId,targetId) != -1)// Проверка на наличие прямого обменного курса
            exchangeRate = getRate(connection,baseId,targetId);
        else if (haveExchangeRate(connection,targetId,baseId) != -1) //Сюда заходит если выполняется проверка на наличие обратного курса
            exchangeRate = 1/getRate(connection,targetId,baseId);
        else//Заключительный случай перекрестного обмена через USD
        {
            int usdId = FindId(connection,USD_CODE);
            double usdToBaseRate = getRate(connection,usdId,baseId);
            double usdToTargetRate = getRate(connection,usdId,targetId);
            exchangeRate = usdToBaseRate/usdToTargetRate;
        }
        return createExchangeRate(baseId,targetId,exchangeRate,Amount);
    }

    private static String createExchangeRate(int baseId, int targetId, double exchangeRate, double amount) {
        ExchangeAmount exchange = new ExchangeAmount();
        exchange.setBaseCurrencyId(baseId);
        exchange.setTargetCurrencyId(targetId);
        exchange.setExchangeRate(Math.round(exchangeRate*100.0)/100.0);
        exchange.setAmount((int)amount);
        exchange.setConvertedAmount(Math.round(exchangeRate*amount*100.0)/100.0);
        return new Gson().toJson(exchange);
    }

    private double getRate(Connection connection, int baseId, int targetId) throws SQLException {
        String query = "SELECT Rate FROM ExchangeRates WHERE BaseCurrencyId = ? AND TargetCurrency = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, baseId);
        preparedStatement.setInt(2,targetId);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.getDouble("Rate");
    }

    private double haveExchangeRate(Connection connection, int baseId, int targetId) {
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

    private int FindId(Connection con, String Currency) throws SQLException {
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
}
