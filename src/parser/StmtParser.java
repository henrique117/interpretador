package parser;

import java.util.ArrayList;
import java.util.List;

import lexer.Token;
import lexer.TokenType;
import parser.interfaces.Expr;
import parser.interfaces.Stmt;
import parser.stmt.*;

public class StmtParser extends Parser {

    public StmtParser(List<Token> tokens) {
        super(tokens);
    }

    public List<Stmt> parse() {
        List<Stmt> statements = new ArrayList<>();

        while (!isAtEnd()) {
            try {
                if (checkTokenType(TokenType.EOF)) break;
                statements.add(statement());
            } catch (ParseError err) {
                System.err.println(err.getMessage());
                synchronize();
            }
        }

        return statements;
    }

    /*

    Gramática:

    program -> statment* | EOF
    statment -> assignStmt | blockStmt | forStmt | ifStmt | printStmt | setStmt | whileStmt
    exprStmt -> exprression ;?
    assignStmt -> IDENTIFIER = exprStmt ;?
    blockStmt -> "{" statment* "}" ;?
    forStmt -> for "(" [setStmt | assignStmt] ; exprStmt ; exprStmt ")" blockStmt
    ifStmt -> if "(" exprStmt ")" blockStmt [else blockStmt]? ;?
    printStmt -> print "(" [[IDENTIFIER | NUMBER | STRING | expr],]+ ")" ;?
    setStmt -> set IDENTIFIER = exprStmt ;?
    while -> while "(" exprStmt ")" blockStmt ;?

    */
    
    /**
     * Algum statement.
     */
    private Stmt statement() {

        if (checkTokenType(TokenType.EOF)) return null;

        while (matchTypes(TokenType.NEW_LINE, TokenType.SEMI_COLUMN)) {
            advance();
        }

        if (matchTypes(TokenType.LEFT_BRACE)) return new BlockStmt(blockStmt());
        if (matchTypes(TokenType.FOR)) return forStmt();
        if (matchTypes(TokenType.IF)) return ifStmt();
        if (matchTypes(TokenType.PRINT)) return printStmt();
        if (matchTypes(TokenType.SET)) return setStmt();
        if (matchTypes(TokenType.WHILE)) return whileStmt();

        if (checkTokenType(TokenType.IDENTIFIER) && checkTokenType(TokenType.EQUAL, peek(1))) return assignStmt();

        if (checkTokenType(TokenType.NUMBER, TokenType.IDENTIFIER, TokenType.LEFT_PAREN, TokenType.TRUE, TokenType.FALSE)) return exprStmt();

        throw error(peek(), "Comando inesperado: '" + peek().getTokenValue() + "'");
    }

    /**
     * Bloco de código.
     */
    private List<Stmt> blockStmt() {
        List<Stmt> statments = new ArrayList<>();

        while (!checkTokenType(TokenType.RIGHT_BRACE) && !isAtEnd()) {
            statments.add(statement());
        }

        consume(TokenType.RIGHT_BRACE, "Esperado '}' após bloco.");

        return statments;
    }

    /**
     * Comando 'for'.
     */
    private Stmt forStmt() {
        consume(TokenType.LEFT_PAREN, "Esperado '(' após 'for'.");

        Stmt initializer = null;

        if (!checkTokenType(TokenType.SEMI_COLUMN)) {
            if (matchTypes(TokenType.SET)) {
                initializer = setStmt();
            } else if (checkTokenType(TokenType.IDENTIFIER)) {
                initializer = assignStmt();
            } else {
                initializer = exprStmt();
            }
        }

        consume(TokenType.SEMI_COLUMN, "Esperado ';' após inicializador.");

        Expr condition = checkTokenType(TokenType.SEMI_COLUMN) ? null : parseExprUntil(TokenType.SEMI_COLUMN);
        consume(TokenType.SEMI_COLUMN, "Esperado ';' após condição.");

        Expr increment = checkTokenType(TokenType.RIGHT_PAREN) ? null : parseExprUntil(TokenType.RIGHT_PAREN);
        consume(TokenType.RIGHT_PAREN, "Esperado ')' após incrementador.");

        Stmt body = statement();
        return new ForStmt(initializer, condition, increment, body);
    }

    /**
     * Comando 'if'.
     */
    private Stmt ifStmt() {
        consume(TokenType.LEFT_PAREN, "Esperado '(' após 'if'.");
        
        Expr condition = parseExprUntil(TokenType.RIGHT_PAREN);
        consume(TokenType.RIGHT_PAREN, "Esperado ')' após condição.");

        Stmt thenBranch = statement();
        Stmt elseBranch = null;
        if (matchTypes(TokenType.ELSE)) {
            elseBranch = statement();
        }

        return new IfStmt(condition, thenBranch, elseBranch);
    }

    /**
     * Comando 'print'.
     */
    private Stmt printStmt() {
        consume(TokenType.LEFT_PAREN, "Esperado '(' após 'print'.");
        List<Expr> values = new ArrayList<>();
        
        do {
            values.add(parseExprUntil(TokenType.RIGHT_PAREN, TokenType.COMMA));
        } while (matchTypes(TokenType.COMMA));

        consume(TokenType.RIGHT_PAREN, "Esperado ')' após argumentos.");

        return new PrintStmt(values);
    }

    /**
     * Comando 'set'.
     */
    private Stmt setStmt() {
        Token name = consume(TokenType.IDENTIFIER, "Esperado nome após 'set'.");
        consume(TokenType.EQUAL, "Esperado '=' após nome.");

        Expr value = parseExprUntil(TokenType.SEMI_COLUMN, TokenType.NEW_LINE);
        return new SetStmt(name, value);
    }

    /**
     * Comando 'while'.
     */
    private Stmt whileStmt() {
        consume(TokenType.LEFT_PAREN, "Esperado '(' após 'while'.");
        Expr condition = parseExprUntil(TokenType.RIGHT_PAREN);

        consume(TokenType.RIGHT_PAREN, "Esperado ')' após condição.");
        Stmt body = statement();

        return new WhileStmt(condition, body);
    }

    /**
     * Mudar valor de variável.
     */
    private Stmt assignStmt() {
        Token name = consume(TokenType.IDENTIFIER, "Esperado um nome antes do '='.");
        consume(TokenType.EQUAL, "Esperado '=' após o identificador.");

        Expr value = parseExprUntil(TokenType.SEMI_COLUMN, TokenType.NEW_LINE);
        return new AssignStmt(name, value);
    }

    /**
     * Expressões lógicas e aritméticas.
     */
    private Stmt exprStmt() {
        Expr expr = parseExprUntil(TokenType.SEMI_COLUMN, TokenType.NEW_LINE);
        return new ExpressionStmt(expr);
    }

    // Helpers

    private Expr parseExprUntil(TokenType... types) {
        List<Token> exprTokens = new ArrayList<>();

        while (!isAtEnd() && !checkTokenType(types)) {
            exprTokens.add(advance());
        }

        ExprParser exprParser = new ExprParser(exprTokens);
        return exprParser.parse();
    }
}