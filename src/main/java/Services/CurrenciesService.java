package Services;

import DAO.CurrenciesDAO;
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

    public JsonArray ProccessGetCurrenciesRequest(Connection connection) throws SQLException {
            List<Currency> currencies = CurrenciesDAO.GetAllCurrencies(connection);
            JsonArray jsonArray;
            if(!currencies.isEmpty()) {
                jsonArray = new Gson().toJsonTree(currencies).getAsJsonArray();
            }
            else {
                jsonArray = new Gson().toJsonTree(new Error("500")).getAsJsonArray();
            }
        // Преобразование списка валют в JSON
        return jsonArray;
    }
    public String ProcessPostCurrenciesRequest(Connection connection, String name, String code, String sign) throws SQLException {
        Currency currency = CurrenciesDAO.AddNewCurrency(connection,name,code,sign);
        return new Gson().toJson(currency);
    }
}
