package parser;

import interfaces.IExpr;
import interpreter.Variable;

public class Literal implements IExpr {
    private final String value;

    public Literal(String value) {
        this.value = value;
    }

    @Override
    public Object evaluate() {
        return Variable.fromString(value).value;
    }
}