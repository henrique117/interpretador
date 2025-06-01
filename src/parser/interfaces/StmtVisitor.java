package parser.interfaces;

import parser.stmt.*;

public interface StmtVisitor<T> {
    T visitAssignStmt(AssignStmt stmt);
    T visitBlockStmt(BlockStmt stmt);
    T visitExpressionStmt(ExpressionStmt stmt);
    T visitForStmt(ForStmt stmt);
    T visitIfStmt(IfStmt stmt);
    T visitPrintStmt(PrintStmt stmt);
    T visitSetStmt(SetStmt stmt);
    T visitWhileStmt(WhileStmt stmt);
}
