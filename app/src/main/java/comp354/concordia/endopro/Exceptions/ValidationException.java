package comp354.concordia.endopro.Exceptions;

public class ValidationException extends Exception {
    public ValidationException(String errMessage){
        super(errMessage);
    }
}
