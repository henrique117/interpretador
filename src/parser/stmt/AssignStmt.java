package parser.stmt;

import lexer.Token;
import parser.interfaces.Expr;
import parser.interfaces.Stmt;
import parser.interfaces.StmtVisitor;

public class AssignStmt implements Stmt {

    public final Token name;
    public final Expr value;

    public AssignStmt(Token name, Expr value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public <T> T accept(StmtVisitor<T> visitor) {
        return visitor.visitAssignStmt(this);
    }
}