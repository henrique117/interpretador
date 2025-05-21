package parser;

import java.util.List;

import interfaces.IExpr;
import interpreter.Variable;
import interpreter.VariableStore;
import utils.Errors;
import utils.Result;

public class BinaryExprParser {

    private final List<String> tokens;
    private int index;
    private final VariableStore variableStore;

    public BinaryExprParser(List<String> tokens, VariableStore variableStore) {
        this.tokens = tokens;
        this.index = 0;
        this.variableStore = variableStore;
    }

    public Result<IExpr> parse() {
        return expression();
    }

    private Result<IExpr> expression() {
        Result<IExpr> exprResult = term();
        if (!exprResult.isOk()) return exprResult;
        IExpr expr = exprResult.getValue();

        while (check("+") || check("-")) {
            String operator = getCurrent();
            gotoNext();
            Result<IExpr> rightResult = term();
            if (!rightResult.isOk()) return rightResult;
            expr = new BinaryExpr(expr, operator, rightResult.getValue());
        }

        return Result.ok(expr);
    }

    private Result<IExpr> term() {
        Result<IExpr> exprResult = factor();
        if (!exprResult.isOk()) return exprResult;
        IExpr expr = exprResult.getValue();

        while (check("*") || check("/")) {
            String operator = getCurrent();
            gotoNext();
            Result<IExpr> rightResult = factor();
            if (!rightResult.isOk()) return rightResult;
            expr = new BinaryExpr(expr, operator, rightResult.getValue());
        }

        return Result.ok(expr);
    }

    private Result<IExpr> factor() {
        if (match("(")) {
            Result<IExpr> exprResult = expression();
            if (!exprResult.isOk()) return exprResult;
            if (!match(")")) return Result.error("Unclosed parenthesis");
            return exprResult;
        }

        String current = getCurrent();

        if (current.matches("-?\\d+(\\.\\d+)?")) {
            gotoNext();
            return Result.ok(new Literal(current));
        }

        if (current.matches("-?[A-Za-z]+.*")) {
            Variable var = variableStore.get(current.trim());

            if (var == null) {
                return Result.error(Errors.variableNotFound(current.trim()));
            }

            gotoNext();
            return Result.ok(new Literal(var.value.toString()));
        }

        return Result.error("Unexpected token: " + current);
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
