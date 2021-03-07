package exception;

public class EngineException extends Exception{

    private String error;

    public EngineException (String error) {
        super(error);
        this.error=error;
    }

    public String getError() {
        return this.error;
    }
}
