package DTO;

public class ExchangeRate extends DataTransferObject {
    private int ID;
    private int BaseCurrencyId;
    private int TargetCurrencyId;
    private double Rate;
    public ExchangeRate() {}
    public ExchangeRate( int ID, int BaseCurrencyId, int TargetCurrencyId, double rate) {
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

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setBaseCurrencyId(int baseCurrencyId) {
        BaseCurrencyId = baseCurrencyId;
    }

    public void setTargetCurrencyId(int targetCurrencyId) {
        TargetCurrencyId = targetCurrencyId;
    }

    public void setRate(double rate) {
        Rate = rate;
    }
}
