package parser.ast;

import lexer.Token;
import parser.interfaces.Expr;
import parser.interfaces.ExprVisitor;

public class UnaryExpr implements Expr {
    
    public final Token operator;
    public final Expr right;

    public UnaryExpr(Token operator, Expr right) {
        this.operator = operator;
        this.right = right;
    }

    @Override
    public <T> T accept(ExprVisitor<T> visitor) {
        return visitor.visitUnaryExpr(this);
    }
}
