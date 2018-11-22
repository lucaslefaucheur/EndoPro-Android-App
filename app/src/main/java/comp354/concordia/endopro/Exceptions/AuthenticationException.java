package comp354.concordia.endopro.Exceptions;

public class AuthenticationException extends Exception {
    public AuthenticationException(String errMessage){
        super(errMessage);
    }
}
