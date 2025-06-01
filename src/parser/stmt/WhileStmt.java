package parser.stmt;

import parser.interfaces.Expr;
import parser.interfaces.Stmt;
import parser.interfaces.StmtVisitor;

public class WhileStmt implements Stmt {
    
    public final Expr condition;
    public final Stmt body;

    public WhileStmt(Expr condition, Stmt body) {
        this.condition = condition;
        this.body = body;
    }
    
    @Override
    public <T> T accept(StmtVisitor<T> visitor) {
        return visitor.visitWhileStmt(this);
    }
}