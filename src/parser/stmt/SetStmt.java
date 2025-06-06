package parser.stmt;

import lexer.Token;
import parser.interfaces.Expr;
import parser.interfaces.Stmt;
import parser.interfaces.StmtVisitor;

public class SetStmt implements Stmt {
    
    public final Token name;
    public final Expr initializer;

    public SetStmt(Token name, Expr initializer) {
        this.name = name;
        this.initializer = initializer;
    }

    @Override
    public <T> T accept(StmtVisitor<T> visitor) {
        return visitor.visitSetStmt(this);
    }
}