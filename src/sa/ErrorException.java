package sa;
import util.Error;

public class ErrorException extends Exception{
    private Error error;
    private String message;


    public ErrorException(Error error, String message){
	this.error = error;
	this.message = message;
    }

    public String getMessage(){
	return message;
    }

    public int getCode(){
	return error.code();
    }

    
}
