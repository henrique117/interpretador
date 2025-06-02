package parser;

import java.util.List;

import lexer.Token;
import lexer.TokenType;
import parser.ast.*;
import parser.interfaces.Expr;

public class ExprParser extends Parser {

    public ExprParser(List<Token> tokens) {
        super(tokens);
    }

    public Expr parse() {
        try {
            Expr expr = expression();
            if (!isAtEnd()) {
                throw error(peek(), "Token '" + peek().getTokenValue() + "' inesperado após término da expressão.");
            }
            return expr;
        } catch (ParseError err) {
            throw error(peek(), err.getMessage());
        }
    }

    /* 

    Gramática:

    expression -> or 
    or -> and | and || and
    and -> equality | equality && equality
    equality -> comparision | comparision == comparision | comparision != comparision
    comparision -> term | term > term | term >= term | term < term | term >= term
    term -> factor | factor + factor | factor - factor
    factor -> power | power * power | power / power | power % power
    power -> unary | unary ** unary
    unary -> primary | !primary | -primary
    primary -> TRUE | FALSE | NUMBER | STRING | IDENTIFIER | (grouping)
    grouping -> expression

    */

    // Recursão de Expressões para formação da AST em ordem de prioridade

    private Expr expression() {
        return or();
    }

    /**
     * Expressões de or: or, ||.
     */
    private Expr or() {
        Expr expr = and();

        while (matchTypes(TokenType.OR)) {
            Token operator = previous();
            Expr right = and();
            expr = new LogicalExpr(expr, operator, right);
        }

        return expr;
    }

    /**
     * Expressões de and: and, &&.
     */
    private Expr and() {
        Expr expr = equality();

        while (matchTypes(TokenType.AND)) {
            Token operator = previous();
            Expr right = equality();
            expr = new LogicalExpr(expr, operator, right);
        }

        return expr;
    }

    /**
     * Expressões de igualdade: ==, !=.
     */
    private Expr equality() {
        Expr expr = comparision();

        while (matchTypes(TokenType.EQUAL_EQUAL, TokenType.NOT_EQUAL)) {
            Token operator = previous();
            Expr right = comparision();
            expr = new BinaryExpr(expr, operator, right);
        }

        return expr;
    }

    /**
     * Expressões de comparação: >, >=, <, <=.
     */
    private Expr comparision() {
        Expr expr = term();

        while (matchTypes(TokenType.GREATER, TokenType.GREATER_EQUAL, TokenType.LESS, TokenType.LESS_EQUAL)) {
            Token operator = previous();
            Expr right = term();
            expr = new BinaryExpr(expr, operator, right);
        }

        return expr;
    }

    /**
     * Expressões de termos: +, -.
     */
    private Expr term() {
        Expr expr = factor();

        while (matchTypes(TokenType.PLUS, TokenType.MINUS)) {
            Token operator = previous();
            Expr right = factor();
            expr = new BinaryExpr(expr, operator, right);
        }

        return expr;
    }

    /**
     * Expressões de fatores: /, *, %.
     */
    private Expr factor() {
        Expr expr = power();

        while (matchTypes(TokenType.MULT, TokenType.DIV, TokenType.MODULE)) {
            Token operator = previous();
            Expr right = power();
            expr = new BinaryExpr(expr, operator, right);
        }

        return expr;
    }

    /**
     * Expressões de potência: **.
     */
    private Expr power() {
        Expr expr = unary();

        if (matchTypes(TokenType.POWER_OF)) {
            Token operator = previous();
            Expr right = power();
            expr = new BinaryExpr(expr, operator, right);
        }

        return expr;
    }

    /**
     * Expressões unárias: !, -, ++, --.
     */
    private Expr unary() {

        // Prefixado: -x, !x, ++i, --i
        if (matchTypes(TokenType.MINUS, TokenType.NOT, TokenType.PLUS_PLUS, TokenType.MINUS_MINUS)) {
            Token operator = previous();
            Expr right = unary(); // Recursivamente resolve o que vem depois
            return new UnaryExpr(operator, right);
        }

        Expr expr = primary(); // Agora chama primary se não for prefixo

        // Pós-fixado: i++ ou i--
        if (matchTypes(TokenType.PLUS_PLUS, TokenType.MINUS_MINUS)) {
            Token operator = previous();
            expr = new PostfixExpr(expr, operator);
        }

        return expr;
    }


    /**
     * Tokens primários: Números, Strings e Booleanos.
     */
    private Expr primary() {
        if (matchTypes(TokenType.TRUE)) return new LiteralExpr(true);
        if (matchTypes(TokenType.FALSE)) return new LiteralExpr(false);
        if (matchTypes(TokenType.NUMBER)) return new LiteralExpr(Double.parseDouble(previous().getTokenValue()));
        if (matchTypes(TokenType.STRING)) return new LiteralExpr(previous().getTokenValue());
        if (matchTypes(TokenType.IDENTIFIER)) return new VariableExpr(previous());

        if (matchTypes(TokenType.RIGHT_PAREN)) {
            if (checkBalance(TokenType.LEFT_PAREN, current)) throw error(previous(), "Parêntese ')' fechado sem abertura.");
        }

        if(matchTypes(TokenType.LEFT_PAREN)) {
            Expr expr = expression();
            consume(TokenType.RIGHT_PAREN, "Esperado ')' após expressão.");
            return new GroupingExpr(expr);
        }

        throw error(peek(), "Esperado expressão.");
    }
}