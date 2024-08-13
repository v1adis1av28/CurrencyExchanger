package Services;

import DAO.ExchangeDAO;
import DTO.Error;
import DTO.ExchangeAmount;
import DTO.ExchangeRate;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static DAO.ExchangeDAO.*;

public class ExchangeService extends ServiceEntity {
    public ExchangeService() {}

    //Логика обмена
    // -Сперва идет проверка прямого курса то есть -> Прмер. Рубли в Тенге -> если есть в ExchangeRates то просто забираем rate
    // -Если нет, прямого проверяем наличие -> Тенге в рубли -> если есть получаем рейт и отсчитываем кол-во для обмена деленное на обратный курс
    // -Если нет ни прямого, ни обратного -> Переводим рубль и тенге в доллар-> значение тенге(usd)/значение рубль(usd) за одну единицу ->получаем rate

    private String USD_CODE = "USD";
    public String ProccesExchange(Connection connection, String baseCurrency, String targetCurrency, String amount) throws SQLException {
        double exchangeRate = 0.0;
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
        //Сокращаем значение до двух знаков после запятой
        exchangeRate = Math.round(exchangeRate*100.0)/100.0;
        double converted = Math.round(exchangeRate*Amount*100.0)/100.0;
        ExchangeAmount ea = ExchangeDAO.createExchangeRate(baseId,targetId,exchangeRate,Amount, converted);
        return new Gson().toJson(ea);
    }

}
