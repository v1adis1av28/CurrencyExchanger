package DTO;

import java.math.BigDecimal;

public class ExchangeAmount extends DataTransferObject{
    private int BaseCurrencyId;
    private int TargetCurrencyId;
    private BigDecimal ExchangeRate;
    private BigDecimal ConvertedAmount;
    private int amount;

    public ExchangeAmount(int baseCurrencyId, int targetCurrencyId, BigDecimal exchangeRate, BigDecimal convertedAmount, int amount) {
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

    public BigDecimal getExchangeRate() {
        return ExchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        ExchangeRate = exchangeRate;
    }

    public BigDecimal getConvertedAmount() {
        return ConvertedAmount;
    }

    public void setConvertedAmount(BigDecimal convertedAmount) {
        ConvertedAmount = convertedAmount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
