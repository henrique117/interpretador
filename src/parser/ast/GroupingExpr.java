package parser.ast;

import parser.interfaces.Expr;
import parser.interfaces.ExprVisitor;

public class GroupingExpr implements Expr {
    
    public final Expr expr;

    public GroupingExpr(Expr expr) {
        this.expr = expr;
    }

    @Override
    public <T> T accept(ExprVisitor<T> visitor) {
        return visitor.visitGroupingExpr(this);
    }
}