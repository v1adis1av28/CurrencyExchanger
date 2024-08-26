package Services;

import DAO.ExchangeRatesDAO;
import DTO.ExchangeRate;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

import Exceptions.CurrencyNotFoundException;
import Exceptions.CurrencyPairExistsException;

import static utils.utils.*;

public class ExchangeRatesService extends ServiceEntity{
    public ExchangeRatesService() {}

    public JsonArray ProccesGetExchangeRates(Connection connection) throws SQLException {
        List<ExchangeRate> exchangeRates = ExchangeRatesDAO.getExchangeRates(connection);
        JsonArray array = new Gson().toJsonTree(exchangeRates).getAsJsonArray();
        return array;
    }

    public String ProccesPostExchangeRates(Connection connection, String BaseId, String TargetId, double Rate) throws SQLException, CurrencyPairExistsException {
        //Сперва будем передавать и проверять наличие каждой id, чтобы если что сразу выкидывать 409(Отсуствует валютная пара)
        //После проверка наличия в базе данных пары (BaseId-TargetId) если нет выкидываем(404)
        //В остальном просто возвращаем json файл или дропаем по 500 ошибке
        if (!CheckId(connection, BaseId) || !CheckId(connection, TargetId)) {
            throw new CurrencyNotFoundException("Currency not found");
        }
        if (CheckPair(connection, BaseId, TargetId)) {
            throw new CurrencyPairExistsException("Currency pair already exists");
        }
        int ID = new Random().nextInt(10000);
        ExchangeRate exchangeRate = new ExchangeRate(ID, findIdByCode(connection, BaseId), findIdByCode(connection, TargetId), Rate);
        ExchangeRatesDAO.InsertExchangeRate(connection, exchangeRate);
        return new Gson().toJson(exchangeRate);
    }


}