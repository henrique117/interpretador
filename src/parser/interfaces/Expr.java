package parser.interfaces;

public interface Expr {
    <T> T accept(ExprVisitor<T> visitor);
}
