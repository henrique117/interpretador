package parser;

import java.util.List;

import lexer.Token;
import lexer.TokenType;
import parser.ast.*;
import parser.interfaces.Expr;

public class ExprParser {
    
    private final List<Token> tokens;
    private int current;

    public ExprParser(List<Token> tokens) {
        this.tokens = tokens;
        current = 0;
    }

    public Expr parse() {
        try {
            Expr expr = expression();
            if (!isAtEnd()) {
                throw error(peek(), "Token '" + peek().getTokenValue() + "' inesperado após término da expressão.");
            }
            return expr;
        } catch (ParseError err) {
            System.err.println(err.getMessage());
            return null;
        }
    }

    // Recursão de Expressões para formação da AST em ordem de prioridade

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
        if (matchTypes(TokenType.RIGHT_PAREN)) {
            throw error(previous(), "Parêntese ')' fechado sem abertura.");
        }

        if (matchTypes(TokenType.TRUE)) return new LiteralExpr(true);
        if (matchTypes(TokenType.FALSE)) return new LiteralExpr(false);
        if (matchTypes(TokenType.NUMBER)) return new LiteralExpr(Double.parseDouble(previous().getTokenValue()));
        if (matchTypes(TokenType.STRING)) return new LiteralExpr(previous().getTokenValue());
        if (matchTypes(TokenType.IDENTIFIER)) return new VariableExpr(previous());

        if(matchTypes(TokenType.LEFT_PAREN)) {
            Expr expr = expression();
            consume(TokenType.RIGHT_PAREN, "Esperado ')' após expressão.");
            return new GroupingExpr(expr);
        }

        throw error(peek(), "Esperado expressão.");
    }

    // Helpers

    // Métodos Booleanos

    /**
     * 
     * @return Se está no final do programa.
     */
    private boolean isAtEnd() {
        return peek().getTokenType() == TokenType.EOF;
    }

    /**
     * 
     * @param expected TokenType esperado.
     * @return Se é o TokenType esperado.
     */
    private boolean checkTokenType(TokenType expected) {
        if (isAtEnd()) return false;
        return peek().getTokenType() == expected;
    }

    /**
     * 
     * @param types Tipo para iteração.
     * @return Enquanto estiver lendo esse tipo.
     */
    private boolean matchTypes(TokenType... types) {
        for (TokenType tokenType : types) {
            if (checkTokenType(tokenType)) {
                advance();
                return true;
            }
        }

        return false;
    }

    // Métodos Token

    /**
     * 
     * @return o token atual.
     */
    private Token peek() {
        return tokens.get(current);
    }

    /**
     * Vai para o próximo token.
     * 
     * @return O úlitimo (Se ele nao puder avançar retorna o EOF).
     */
    private Token advance() {
        if (isAtEnd()) return peek();
        current++;
        return previous();
    }

    /**
     * 
     * @return O último token.
     */
    private Token previous() {
        return tokens.get(current - 1);
    }

    /**
     * 
     * @param type TokeType do token a ser consumido.
     * @param errMessage Mensagem de erro.
     * @return O token consumido.
     */
    private Token consume(TokenType type, String errMessage) {
        if (checkTokenType(type)) return advance();
        throw error(peek(), errMessage);
    }

    // Tratamento de erro

    /**
     * Joga um erro no sistema.
     * 
     * @param token Token que foi achado o erro.
     * @param errMessage Mensagem de erro.
     * @return Erro.
     */
    private ParseError error(Token token, String errMessage) {
        throw new ParseError("Erro em linha: " + token.getTokenLine() + ":" + token.getTokenCaracter() + ": " + errMessage);
    }
}