package parser;

public class ParseError extends RuntimeException {
    public ParseError(String errMessage) {
        super(errMessage);
    }
}