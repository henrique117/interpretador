package parser;

import interfaces.IExpr;
import interpreter.Variable;
import utils.Result;

public class Literal implements IExpr {
    private final String value;

    public Literal(String value) {
        this.value = value;
    }

    @Override
    public Result<Object> evaluate() {
        try {
            return Result.ok(Variable.fromString(value).value);
        } catch (Exception e) {
            return Result.error("Failed to parse literal: \"" + value + "\"");
        }
    }
}