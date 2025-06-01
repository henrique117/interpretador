package parser.interfaces;

import parser.ast.BinaryExpr;
import parser.ast.GroupingExpr;
import parser.ast.LiteralExpr;
import parser.ast.LogicalExpr;
import parser.ast.UnaryExpr;
import parser.ast.VariableExpr;

public interface ExprVisitor<T> {
    T visitBinaryExpr(BinaryExpr expr);
    T visitGroupingExpr(GroupingExpr expr);
    T visitLiteralExpr(LiteralExpr expr);
    T visitLogicalExpr(LogicalExpr expr);
    T visitUnaryExpr(UnaryExpr expr);
    T visitVariableExpr(VariableExpr expr);
}