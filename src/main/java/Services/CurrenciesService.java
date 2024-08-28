package Services;
import DAO.CurrenciesDAO;
import DTO.Currency;
import Exceptions.CurrencyNotFoundException;
import Exceptions.DatabaseException;
import Exceptions.InvalidInputException;
import Exceptions.NotUniqueObjectException;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import DTO.Error;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static utils.utils.findIdByCode;

public class CurrenciesService extends ServiceEntity{
    public  CurrenciesService() {}

    public JsonArray ProccessGetCurrenciesRequest(Connection connection) {
        try {
            List<Currency> currencies = CurrenciesDAO.GetAllCurrencies(connection);
            if (currencies.isEmpty()) {
                throw new CurrencyNotFoundException("No currencies found");
            }
            return new Gson().toJsonTree(currencies).getAsJsonArray();
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving currencies from database", e);
        }
    }

    public String ProcessPostCurrenciesRequest(Connection connection, String name, String code, String sign) throws SQLException, NotUniqueObjectException, InvalidInputException {
        if(findIdByCode(connection,code) != -1)
        {
            throw new NotUniqueObjectException("Currencies not unique");
        }
        if(!code.chars().allMatch(Character::isLetter) && code.length() == 3)
        {
            throw new InvalidInputException("Wrong input values");
        }
        Currency currency = CurrenciesDAO.AddNewCurrency(connection,name,code,sign);
        return new Gson().toJson(currency);
    }
}
