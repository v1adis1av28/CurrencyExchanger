package DTO;

public class ExchangeRate extends DataTransferObject {
    private int ID;
    private int BaseCurrencyId;
    private int TargetCurrencyId;
    private double Rate;
    private String BaseCurrencyCode;  // Добавлено для хранения кода базовой валюты
    private String TargetCurrencyCode; // Добавлено для хранения кода целевой валюты

    public ExchangeRate() {
    }

    public String getTargetCurrencyCode() {
        return TargetCurrencyCode;
    }

    public void setTargetCurrencyCode(String targetCurrencyCode) {
        TargetCurrencyCode = targetCurrencyCode;
    }

    public void setBaseCurrencyCode(String baseCurrencyCode) {
        BaseCurrencyCode = baseCurrencyCode;
    }

    public ExchangeRate(int ID, int BaseCurrencyId, int TargetCurrencyId, double rate) {
        this.ID = ID;
        this.BaseCurrencyId = BaseCurrencyId;
        this.TargetCurrencyId = TargetCurrencyId;
        this.Rate = rate;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getBaseCurrencyId() {
        return BaseCurrencyId;
    }

    public void setBaseCurrencyId(int baseCurrencyId) {
        BaseCurrencyId = baseCurrencyId;
    }

    public int getTargetCurrencyId() {
        return TargetCurrencyId;
    }

    public void setTargetCurrencyId(int targetCurrencyId) {
        TargetCurrencyId = targetCurrencyId;
    }

    public double getRate() {
        return Rate;
    }

    public void setRate(double rate) {
        Rate = rate;
    }

    public String getBaseCurrencyCode() {
        return BaseCurrencyCode;
    }
}
