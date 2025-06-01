package parser.interfaces;

public interface Stmt {
    <T> T accept(StmtVisitor<T> visitor);
}