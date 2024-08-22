package Services;

import DAO.ExchangeRatesDAO;
import DTO.Error;
import DTO.ExchangeRate;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static utils.utils.*;

public class ExchangeRatesService extends ServiceEntity{
    public ExchangeRatesService() {}

    public JsonArray ProccesGetExchangeRates(Connection connection) throws SQLException {
        List<ExchangeRate> exchangeRates = ExchangeRatesDAO.getExchangeRates(connection);
        JsonArray array = new Gson().toJsonTree(exchangeRates).getAsJsonArray();
        return array;
    }

    public String ProccesPostExchangeRates(Connection connection, String BaseId, String TargetId, double Rate) throws SQLException {
        //Сперва будем передавать и проверять наличие каждой id, чтобы если что сразу выкидывать 409(Отсуствует валютная пара)
        //После проверка наличия в базе данных пары (BaseId-TargetId) если нет выкидываем(404)
        //В остальном просто возвращаем json файл или дропаем по 500 ошибке
        if(!CheckId(connection,BaseId) || !CheckId(connection,TargetId))
            return new Gson().toJson(new Error("404"));
        if(CheckPair(connection,BaseId,TargetId))//Если валюта возвращает True-> значит пара есть и возвращаем ошибку
            return new Gson().toJson(new Error("409"));
        int ID = FindMaxId(connection)+1;
        ExchangeRate exchangeRate = new ExchangeRate(ID,findIdByCode(connection,BaseId),findIdByCode(connection,TargetId),Rate);
        ExchangeRatesDAO.InsertExchangeRate(connection,exchangeRate);
        return new Gson().toJson(exchangeRate);
    }


}