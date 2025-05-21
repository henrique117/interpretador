package parser;

import java.util.List;

import interfaces.IExpr;

public class BinaryExprParser {

    private final List<String> tokens;
    private int index;

    public BinaryExprParser(List<String> tokens) {
        this.tokens = tokens;
        this.index = 0;
    }

    public IExpr parse() {
        return expression();
    }

    private IExpr expression() {
        IExpr expr = term();

        while (check("+") || check("-")) {
            String operator = getCurrent();
            gotoNext();
            IExpr right = term();
            expr = new BinaryExpr(expr, operator, right);
        }

        return expr;
    }

    private IExpr term() {
        IExpr expr = factor();

        while (check("*") || check("/")) {
            String operator = getCurrent();
            gotoNext();
            IExpr right = factor();
            expr = new BinaryExpr(expr, operator, right);
        }

        return expr;
    }

    private IExpr factor() {
        if (match("(")) {
            IExpr expr = expression();
            if (!match(")")) throw new RuntimeException("Unclosed parenthesis");
            return expr;
        }

        String current = getCurrent();

        if (current.matches("-?\\d+(\\.\\d+)?")) {
            gotoNext();
            return new Literal(current);
        }

        throw new RuntimeException("Unexpected token: " + current);
    }

    private String getCurrent() {
        return tokens.get(index);
    }

    private void gotoNext() {
        if (index + 1 <= tokens.size()) index++;
    }

    private boolean match(String expected) {
        if (check(expected)) {
            gotoNext();
            return true;
        }
        return false;
    }

    private boolean check(String expected) {
        return !isAtEnd() && getCurrent().equals(expected);
    }

    private boolean isAtEnd() {
        return index >= tokens.size();
    }
}