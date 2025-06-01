package parser.stmt;

import parser.interfaces.Expr;
import parser.interfaces.Stmt;
import parser.interfaces.StmtVisitor;

public class ForStmt implements Stmt {
    
    public final Stmt initializer;
    public final Expr condition;
    public final Expr increment;
    public final Stmt body;

    public ForStmt(Stmt initializer, Expr condition, Expr increment, Stmt body) {
        this.initializer = initializer;
        this.condition = condition;
        this.increment = increment;
        this.body = body;
    }

    @Override
    public <T> T accept(StmtVisitor<T> visitor) {
        return visitor.visitForStmt(this);
    }
}