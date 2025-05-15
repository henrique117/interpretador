package parser;

import interfaces.IExpr;

public class BinaryExpr implements IExpr {
    private final IExpr left;
    private final String operator;
    private final IExpr right;

    public BinaryExpr(IExpr left, String operator, IExpr right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public Object evaluate() {
        Object leftVal = left.evaluate();
        Object rightVal = right.evaluate();

        if (leftVal instanceof Number && rightVal instanceof Number) {
            double l = ((Number) leftVal).doubleValue();
            double r = ((Number) rightVal).doubleValue();

            switch (operator) {
                case "+": return l + r;
                case "-": return l - r;
                case "*": return l * r;
                case "/": return l / r;
                default: 
                    throw new UnsupportedOperationException("Operador '" + operator + "' não suportado");
            }
        }

        throw new UnsupportedOperationException("Operação inválida entre tipos");
    }
}