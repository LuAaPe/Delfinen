package UI;

public class InvalidBirthYearException extends IllegalArgumentException{
    public InvalidBirthYearException(String message){
        super(message);
    }
}
