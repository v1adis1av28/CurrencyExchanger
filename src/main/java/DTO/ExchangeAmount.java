package DTO;

public class ExchangeAmount extends DataTransferObject{
    private int BaseCurrencyId;
    private int TargetCurrencyId;
    private double ExchangeRate;
    private double ConvertedAmount;
    private int amount;

    public ExchangeAmount(int baseCurrencyId, int targetCurrencyId, double exchangeRate, double convertedAmount, int amount) {
        BaseCurrencyId = baseCurrencyId;
        TargetCurrencyId = targetCurrencyId;
        ExchangeRate = exchangeRate;
        ConvertedAmount = convertedAmount;
        this.amount = amount;
    }
    public ExchangeAmount()
    {}

    @Override
    public String toString() {
        return "ExchangeAmount{" +
                "BaseCurrencyId=" + BaseCurrencyId +
                ", TargetCurrencyId=" + TargetCurrencyId +
                ", ExchangeRate=" + ExchangeRate +
                ", ConvertedAmount=" + ConvertedAmount +
                ", amount=" + amount +
                '}';
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

    public double getExchangeRate() {
        return ExchangeRate;
    }

    public void setExchangeRate(double exchangeRate) {
        ExchangeRate = exchangeRate;
    }

    public double getConvertedAmount() {
        return ConvertedAmount;
    }

    public void setConvertedAmount(double convertedAmount) {
        ConvertedAmount = convertedAmount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
