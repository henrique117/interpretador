package parser.ast;

import lexer.Token;
import parser.interfaces.Expr;
import parser.interfaces.ExprVisitor;

public class LogicalExpr implements Expr {
    
    public final Expr left;
    public final Token operator;
    public final Expr right;

    public LogicalExpr(Expr left, Token operator, Expr right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public <T> T accept(ExprVisitor<T> visitor) {
        return visitor.visitLogicalExpr(this);
    }
}
