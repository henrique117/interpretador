package parser.stmt;

import java.util.List;

import parser.interfaces.Stmt;
import parser.interfaces.StmtVisitor;

public class BlockStmt implements Stmt {
    
    public final List<Stmt> statements;

    public BlockStmt(List<Stmt> statements) {
        this.statements = statements;
    }

    @Override
    public <T> T accept(StmtVisitor<T> visitor) {
        return visitor.visitBlockStmt(this);
    }
}
