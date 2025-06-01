package debug;

import java.util.List;

import lexer.Token;

public class TokensPrinter {
    private List<Token> tokens;

    public TokensPrinter(List<Token> tokens) {
        this.tokens = tokens;
    }

    public String print() {
        StringBuilder builder = new StringBuilder();

        for (Token token : tokens) {
            builder.append(token);
            builder.append("\n");
        }

        return builder.toString();
    }
}
