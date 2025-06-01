package parser.ast;

import parser.interfaces.Expr;
import parser.interfaces.ExprVisitor;

public class LiteralExpr implements Expr {
    
    public final Object value;

    public LiteralExpr(Object value) {
        this.value = value;
    }

    @Override
    public <T> T accept(ExprVisitor<T> visitor) {
        return visitor.visitLiteralExpr(this);
    }
}