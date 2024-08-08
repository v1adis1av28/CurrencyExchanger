package Services;

import DTO.Currency;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import DTO.Error;
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
    public String ProcessPostCurrenciesRequest(Statement statement, String name, String code, String sign) {
        Gson gson = new Gson();//Объект для передачи в json сервлета
        try
        {
            int ID = GenerateID(statement);
            String sql = "insert into currencies values ( " + ID + ", '" + code + "', '" +name  + "', '" + sign + "');";
            statement.executeUpdate(sql);
            DTO.Currency currency = new DTO.Currency();
            currency.setID(ID);
            currency.setCode(code);
            currency.setFullName(name);
            currency.setSign(sign);
            return gson.toJson(currency);
        } catch (SQLException e) {
            return gson.toJson(new Error("409"));// Ошибка при добавлении(Валюта с таким кодом уже существует)
        }
    }
    private int GenerateID(Statement statement) throws SQLException {
        Random rand = new Random();
        while(true)
        {
            int randomID = rand.nextInt(1000);
            String sql = "select * from currencies where ID="+randomID + ";";
            ResultSet resultSet = statement.executeQuery(sql);
            if(!resultSet.next())
            {
                return randomID;
            }
            else
                continue;
        }
    }
}
