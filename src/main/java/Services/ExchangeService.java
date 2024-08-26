package Services;

import DAO.ExchangeDAO;
import DTO.Error;
import DTO.ExchangeAmount;
import Exceptions.CurrencyNotFoundException;
import Exceptions.ExchangeRateNotFoundException;
import Exceptions.InvalidAmountException;
import com.google.gson.Gson;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

public class ExchangeService extends ServiceEntity {
    public ExchangeService() {}
    private String USD_CODE = "USD";
    //Логика обмена
    // -Сперва идет проверка прямого курса то есть -> Прмер. Рубли в Тенге -> если есть в ExchangeRates то просто забираем rate
    // -Если нет, прямого проверяем наличие -> Тенге в рубли -> если есть получаем рейт и отсчитываем кол-во для обмена деленное на обратный курс
    // -Если нет ни прямого, ни обратного -> Переводим рубль и тенге в доллар-> значение тенге(usd)/значение рубль(usd) за одну единицу ->получаем rate

    public String ProccesExchange(Connection connection, String baseCurrency, String targetCurrency, String amount) throws SQLException, InvalidAmountException {
        double exchangeRate;
        double Amount;
        try {
            Amount = Double.parseDouble(amount);
        } catch (NumberFormatException e) {
            throw new InvalidAmountException("Invalid amount format");
        }
        int baseId = ExchangeDAO.FindId(connection, baseCurrency);
        int targetId = ExchangeDAO.FindId(connection, targetCurrency);
        try {
            if (ExchangeDAO.haveExchangeRate(connection, baseId, targetId) != -1) {
                exchangeRate = ExchangeDAO.getRate(connection, baseId, targetId);
            } else if (ExchangeDAO.haveExchangeRate(connection, targetId, baseId) != -1) {
                exchangeRate = 1 / ExchangeDAO.getRate(connection, targetId, baseId);
            } else {
                int usdId = ExchangeDAO.FindId(connection, USD_CODE);
                double usdToBaseRate = ExchangeDAO.getRate(connection, usdId, baseId);
                double usdToTargetRate = ExchangeDAO.getRate(connection, usdId, targetId);
                exchangeRate = usdToBaseRate / usdToTargetRate;
            }
            exchangeRate = Math.round(exchangeRate * 100.0) / 100.0;
            double converted = Math.round(exchangeRate * Amount * 100.0) / 100.0;
            ExchangeAmount ea = ExchangeDAO.createExchangeRate(baseId, targetId, exchangeRate, Amount, converted);
            return new Gson().toJson(ea);
        } catch (CurrencyNotFoundException | ExchangeRateNotFoundException e) {
            return new Gson().toJson(new Error("404"));
        }
    }
}
