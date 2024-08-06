package DTO;

public class Currency extends DataTransferObject{
    private int ID;
    private String Code;
    private String FullName;
    private String Sign;

    public Currency(int ID, String Code, String FullName, String Sign) {
        this.ID = ID;
        this.Code = Code;
        this.FullName = FullName;
        this.Sign = Sign;
    }

    public Currency() {}

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setCode(String code) {
        Code = code;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public void setSign(String sign) {
        Sign = sign;
    }

    public int getID() {
        return ID;
    }

    public String getSign() {
        return Sign;
    }

    public String getFullName() {
        return FullName;
    }

    public String getCode() {
        return Code;
    }

    @Override
    public String toString() {
        return "Currency{" +
                "ID=" + ID +
                ", Code='" + Code + '\'' +
                ", FullName='" + FullName + '\'' +
                ", Sign='" + Sign + '\'' +
                '}';
    }
}
