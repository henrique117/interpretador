package parser;

import interfaces.IExpr;
import utils.Result;

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
    public Result<Object> evaluate() {
        Result<Object> leftResult = left.evaluate();
        if (!leftResult.isOk()) return leftResult;

        Result<Object> rightResult = right.evaluate();
        if (!rightResult.isOk()) return rightResult;

        Object leftVal = leftResult.getValue();
        Object rightVal = rightResult.getValue();

        if (leftVal instanceof Number && rightVal instanceof Number) {
            double l = ((Number) leftVal).doubleValue();
            double r = ((Number) rightVal).doubleValue();

            return switch (operator) {
                case "+" -> Result.ok(l + r);
                case "-" -> Result.ok(l - r);
                case "*" -> Result.ok(l * r);
                case "/" -> Result.ok(r == 0 ? null : l / r);
                default -> Result.error("Operator '" + operator + "' is not supported");
            };
        }

        return Result.error("Invalid operation between non-numeric values");
    }
}