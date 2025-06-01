package lexer;

public class Token {
    
    private TokenType tokenType;
    private String tokenValue;
    private final int tokenLine;
    private final int tokenCaracter;
    public String value;

    public Token(TokenType tokenType, String tokenValue, int tokenLine, int tokenCaracter) {
        this.tokenType = tokenType;
        this.tokenValue = tokenValue;
        this.tokenLine = tokenLine;
        this.tokenCaracter = tokenCaracter;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public String getTokenValue() {
        return tokenValue;
    }

    public int getTokenLine() {
        return tokenLine;
    }

    public int getTokenCaracter() {
        return tokenCaracter;
    }

    @Override
    public String toString() {
        return String.format("Token(%s, \"%s\") - Line: %s, Caracter: %s", getTokenType(), getTokenValue(), getTokenLine(), getTokenCaracter());
    }
}