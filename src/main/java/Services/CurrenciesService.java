package Services;

import DTO.Currency;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public class CurrenciesService extends ServiceEntity{
    public  CurrenciesService() {}

    //Обработчик данных по запросу из сервлета
    //Вызывается из CurrenciesServelet.doGet
    public JsonArray ProccessGetCurrenciesRequest(Statement statement) {
        String sql = "select  *  from currencies";
        try {
            ResultSet resultSet = statement.executeQuery(sql);
            List<Currency> currencies = new ArrayList<>();
            while (resultSet.next()) {
                DTO.Currency currency = new DTO.Currency();
                currency.setID(resultSet.getInt("ID"));
                currency.setCode(resultSet.getString("Code"));
                currency.setFullName(resultSet.getString("FullName"));
                currency.setSign(resultSet.getString("Sign"));
                currencies.add(currency);
            }
            // Преобразование списка валют в JSON
            JsonArray jsonArray = new Gson().toJsonTree(currencies).getAsJsonArray();
            return jsonArray;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //TODOO нужно два метода
    // Метод который будет обрабатывать запрос для ДТО с POST запросом
    // Второй нужен для проверки валидности вставляемого id(который генерим)
}
