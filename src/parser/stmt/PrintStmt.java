package parser.stmt;

import java.util.List;

import parser.interfaces.Expr;
import parser.interfaces.Stmt;
import parser.interfaces.StmtVisitor;

public class PrintStmt implements Stmt {

    public final List<Expr> expressions;

    public PrintStmt(List<Expr> expressions) {
        this.expressions = expressions;
    }

    @Override
    public <T> T accept(StmtVisitor<T> visitor) {
        return visitor.visitPrintStmt(this);
    }
}