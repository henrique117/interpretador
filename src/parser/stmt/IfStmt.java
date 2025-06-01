package parser.stmt;

import parser.interfaces.Expr;
import parser.interfaces.Stmt;
import parser.interfaces.StmtVisitor;

public class IfStmt implements Stmt {
    
    public final Expr condition;
    public final Stmt thenBranch;
    public final Stmt elseBranch;

    public IfStmt(Expr condition, Stmt thenBranch, Stmt elseBranch) {
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    @Override
    public <T> T accept(StmtVisitor<T> visitor) {
        return visitor.visitIfStmt(this);
    }
}
