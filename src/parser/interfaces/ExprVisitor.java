package parser.interfaces;

import parser.ast.*;

public interface ExprVisitor<T> {
    T visitBinaryExpr(BinaryExpr expr);
    T visitGroupingExpr(GroupingExpr expr);
    T visitLiteralExpr(LiteralExpr expr);
    T visitLogicalExpr(LogicalExpr expr);
    T visitUnaryExpr(UnaryExpr expr);
    T visitVariableExpr(VariableExpr expr);
    T visitPostfixExpr(PostfixExpr postfixExpr);
}