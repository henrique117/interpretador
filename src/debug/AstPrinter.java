package debug;

import parser.ast.*;
import parser.interfaces.Expr;
import parser.interfaces.ExprVisitor;

public class AstPrinter implements ExprVisitor<String> {

    public String print(Expr expr) {
        if(expr != null) return expr.accept(this);
        return "Árvore teve um erro na sua construção";
    }

    @Override
    public String visitBinaryExpr(BinaryExpr expr) {
        return parenthesize(expr.operator.getTokenValue(), expr.left, expr.right);
    }

    @Override
    public String visitGroupingExpr(GroupingExpr expr) {
        return parenthesize("group", expr.expr);
    }

    @Override
    public String visitLiteralExpr(LiteralExpr expr) {
        return expr.value == null ? "null" : expr.value.toString();
    }

    @Override
    public String visitLogicalExpr(LogicalExpr expr) {
        return parenthesize(expr.operator.getTokenValue(), expr.left, expr.right);
    }

    @Override
    public String visitUnaryExpr(UnaryExpr expr) {
        return parenthesize(expr.operator.getTokenValue(), expr.right);
    }

    @Override
    public String visitVariableExpr(VariableExpr expr) {
        return expr.name.getTokenValue();
    }

    @Override
    public String visitPostfixExpr(PostfixExpr expr) {
        // Adiciona "post" antes do operador para deixar claro que é pós-fixado
        return parenthesize("post " + expr.operator.getTokenValue(), expr.target);
    }

    private String parenthesize(String name, Expr... exprs) {
        StringBuilder builder = new StringBuilder();
        builder.append("(").append(name);

        for (Expr expr : exprs) {
            builder.append(" ");
            builder.append(expr.accept(this));
        }

        builder.append(")");
        return builder.toString();
    }
}