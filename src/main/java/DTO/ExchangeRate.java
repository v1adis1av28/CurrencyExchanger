package DTO;

public class ExchangeRate extends DataTransferObject {
    private int ID;
    private int BaseCurrencyId;
    private int TargetCurrencyId;
    private double Rate;
    public ExchangeRate(double rate, int ID, int BaseCurrencyId, int TargetCurrencyId) {
        this.Rate = rate;
        this.ID = ID;
        this.BaseCurrencyId = BaseCurrencyId;
        this.TargetCurrencyId = TargetCurrencyId;
    }
    public double getRate() {
        return Rate;
    }

    public int getTargetCurrencyId() {
        return TargetCurrencyId;
    }

    public int getBaseCurrencyId() {
        return BaseCurrencyId;
    }

    public int getID() {
        return ID;
    }
}
