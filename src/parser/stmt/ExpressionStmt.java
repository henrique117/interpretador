package parser.stmt;

import parser.interfaces.Expr;
import parser.interfaces.Stmt;
import parser.interfaces.StmtVisitor;

public class ExpressionStmt implements Stmt {
    
    public final Expr expression;

    public ExpressionStmt(Expr expression) {
        this.expression = expression;
    }

    @Override
    public <T> T accept(StmtVisitor<T> visitor) {
        return visitor.visitExpressionStmt(this);
    }
}
