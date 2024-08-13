package Services;

import DAO.ExchangeRateDAO;
import DTO.Error;
import DTO.ExchangeRate;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static utils.utils.isCodeValid;

public class ExchangeRateService extends ServiceEntity {
    public ExchangeRateService() {
    }

    public String ProccesGetExchangeRate(Connection connection, String requestInfo) throws SQLException {
        String firstCode = requestInfo.substring(0, 3);
        String secondCode = requestInfo.substring(3, requestInfo.length());
        //Сделать проверку на наличие кодов в базе данных, если нет их ->404
        if (!isCodeValid(connection, firstCode) || !isCodeValid(connection, secondCode)) {
            return new Gson().toJson(new Error("404"));
        }
        ExchangeRate exchangeRate = ExchangeRateDAO.getExchangeRate(connection, firstCode, secondCode);
        return new Gson().toJson(exchangeRate);
    }

    public String ProccesPatchExchangeRate(Connection connection, double newRate, String requestInfo) throws SQLException {
        String firstCode = requestInfo.substring(0, 3);
        String secondCode = requestInfo.substring(3, requestInfo.length());
        if (!isCodeValid(connection, firstCode) || !isCodeValid(connection, secondCode) || !CheckPair(connection, firstCode, secondCode)) {
            return new Gson().toJson(new Error("404"));
        }
        ExchangeRate er = ExchangeRateDAO.patchExchangeRate(connection, newRate, firstCode, secondCode);
        return new Gson().toJson(er);
    }

    private boolean CheckPair(Connection con, String firstCode, String secondCode) throws SQLException {
        return ExchangeRateDAO.isPair(con, firstCode, secondCode);
    }
}

