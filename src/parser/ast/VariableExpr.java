package parser.ast;

import lexer.Token;
import parser.interfaces.Expr;
import parser.interfaces.ExprVisitor;

public class VariableExpr implements Expr {
    
    public final Token name;

    public VariableExpr(Token name) {
        this.name = name;
    }

    @Override
    public <T> T accept(ExprVisitor<T> visitor) {
        return visitor.visitVariableExpr(this);
    }
}