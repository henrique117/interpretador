package lexer;

public enum TokenType {
    IDENTIFIER,
    NUMBER,
    STRING,
    BOOLEAN,

    LEFT_PAREN,
    RIGHT_PAREN,
    LEFT_BRACE,
    RIGHT_BRACE,
    LEFT_BRACKET,
    RIGHT_BRACKET,

    NEW_LINE,

    IF,
    ELSE,
    WHILE,
    FOR,
    PRINT,
    SET,

    COMMA,
    EQUAL,
    DOUBLE_QUOTES,
    SINGLE_QUOTES,

    NOT,
    EQUAL_EQUAL,
    NOT_EQUAL,
    GREATER,
    GREATER_EQUAL,
    LESS,
    LESS_EQUAL,
    AND,
    OR,

    PLUS,
    MINUS,
    MULT,
    DIV,
    POWER_OF,
    MODULE
}