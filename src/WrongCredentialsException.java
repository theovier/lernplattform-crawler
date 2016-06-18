import java.io.IOException;

public class WrongCredentialsException extends IOException {

    public WrongCredentialsException() { }

    public WrongCredentialsException(String message)
    {
        super(message);
    }

    public WrongCredentialsException(Throwable cause)
    {
        super(cause);
    }

    public WrongCredentialsException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
