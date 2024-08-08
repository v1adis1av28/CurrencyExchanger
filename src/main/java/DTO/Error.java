package DTO;

public class Error extends DataTransferObject{
    private String error;
    public Error(String error){
        this.error = error;
    }
    public void setError(String error) {
        this.error = error;
    }
    public String getError() {
        return error;
    }
}
