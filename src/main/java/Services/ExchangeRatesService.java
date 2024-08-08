package Services;

import DTO.Error;
import DTO.ExchangeRate;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRatesService extends ServiceEntity{
    public ExchangeRatesService() {}

    public JsonArray ProccesGetExchangeRates(Statement statement) throws SQLException {
        String sql = "select * from ExchangeRates";
        JsonArray array = new JsonArray();
        try (ResultSet resultSet = statement.executeQuery(sql)) {
            List<ExchangeRate> exchangeRates = new ArrayList<>();
            while (resultSet.next()) {
                DTO.ExchangeRate er = new DTO.ExchangeRate();
                er.setID(resultSet.getInt("ID"));
                er.setBaseCurrencyId(resultSet.getInt("BaseCurrencyId"));
                er.setTargetCurrencyId(resultSet.getInt("TargetCurrency"));
                er.setRate(resultSet.getDouble("Rate"));
                exchangeRates.add(er);
            }
            // Преобразование списка валют в JSON
            JsonArray jsonArray = new Gson().toJsonTree(exchangeRates).getAsJsonArray();
            return jsonArray;
        }
        catch (SQLException e) {
            array.add(new Gson().toJson(new Error("500")));
            return array;
        }
    }

    public String ProccesPostExchangeRates(Statement statement, int BaseId, int TargetId, double Rate) throws SQLException {
        //Сперва будем передавать и проверять наличие каждой id, чтобы если что сразу выкидывать 409(Отсуствует валютная пара)
        //После проверка наличия в базе данных пары (BaseId-TargetId) если нет выкидываем(404)
        //В остальном просто возвращаем json файл или дропаем по 500 ошибке
        if(!CheckId(statement,BaseId) || !CheckId(statement,TargetId))
            return new Gson().toJson(new Error("404"));
        if(CheckPair(statement,BaseId,TargetId))//Если валюта возвращает True-> значит пара есть и возвращаем ошибку
            return new Gson().toJson(new Error("409"));
        int ID = FindMaxId(statement)+1;
        String sql = "Insert into ExchangeRates(ID,BaseCurrencyId,TargetCurrency,Rate) values("+ID+","+BaseId+","+TargetId+","+Rate+");";
        statement.executeUpdate(sql);
        ExchangeRate exchangeRate = new ExchangeRate(ID,BaseId,TargetId,Rate);
        return new Gson().toJson(exchangeRate);
    }

    //ф-я которая находит максимальное значение id из таблицы
    int FindMaxId(Statement statement) throws SQLException {
        String sql = "select Max(ID) from ExchangeRates";
        ResultSet resultSet = statement.executeQuery(sql);
        if(resultSet.next())
        {
            return resultSet.getInt(1);
        }
        else
            return 1;
    }
    //Здесь проверяем наличие самой id в базе данных, данные тянем из Currencies
    boolean CheckId(Statement statement, int id) throws SQLException {
        String sql = "select * from Currencies where ID=" + id + ";";
        try {
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Проверяем наличие пары, если прошла проверку на наличие валют
    boolean CheckPair(Statement statement, int baseId, int targetId) throws SQLException {
        String sql = "select * from ExchangeRates WHERE BaseCurrencyId=" + baseId + " AND TargetCurrency=" + targetId + ";";
        try (ResultSet resultSet = statement.executeQuery(sql)) {
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
