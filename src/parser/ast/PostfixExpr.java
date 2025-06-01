package parser.ast;

import lexer.Token;
import parser.interfaces.Expr;
import parser.interfaces.ExprVisitor;

public class PostfixExpr implements Expr {
    public final Expr target;
    public final Token operator;

    public PostfixExpr(Expr target, Token operator) {
        this.target = target;
        this.operator = operator;
    }

    @Override
    public <T> T accept(ExprVisitor<T> visitor) {
        return visitor.visitPostfixExpr(this);
    }
}