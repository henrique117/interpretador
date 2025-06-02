package parser;

import java.util.List;

import lexer.Token;
import lexer.TokenType;

public abstract class Parser {

    protected List<Token> tokens;
    protected int current;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.current = 0;
    }

    // Helpers

    // Métodos Booleanos

    /**
     * 
     * @return Se está no final do programa.
     */
    protected boolean isAtEnd() {
        return (peek().getTokenType() == TokenType.EOF) || current >= tokens.size();
    }

    /**
     * 
     * @param expected TokenType esperado.
     * @param token Token.
     * @return Se é o TokenType esperado.
     */
    protected boolean checkTokenType(TokenType expected, Token token) {
        if (isAtEnd()) return false;
        return token.getTokenType() == expected;
    }

    /**
     * 
     * @param expected TokenType esperado.
     * @return Se é o TokenType esperado.
     */
    protected boolean checkTokenType(TokenType expected) {
        return checkTokenType(expected, peek());
    }

    protected boolean checkTokenType(TokenType... types) {
        for (TokenType type : types) {
            if (checkTokenType(type)) return true;
        }
        return false;
    }

    /**
     * 
     * @param types Tipo para iteração.
     * @return Enquanto estiver lendo esse tipo.
     */
    protected boolean matchTypes(TokenType... types) {
        for (TokenType tokenType : types) {
            if (checkTokenType(tokenType, peek())) {
                advance();
                return true;
            }
        }

        return false;
    }

    /**
     * 
     * @param type O parâmetro que está sendo buscado
     * @param index O início da busca
     * @return Se existe um correspondente anterior
     */
    protected boolean checkBalance(TokenType type, int index) {
        if (index - 1 >= 0 && tokens.get(index - 1).getTokenType() != type) return checkBalance(type, index - 1);
        if (index - 1 < 0) return false;
        return true; 
    }

    // Métodos Token

    /**
     * 
     * @return o token atual.
     */
    protected Token peek() {
        return peek(0);
    }

    /**
     * 
     * @param offset Quanto você quer ver a frente.
     * @return o token.
     */
    protected Token peek(int offset) {
        if (current + offset >= tokens.size()) {
            return tokens.get(tokens.size() - 1);
        }
        return tokens.get(current + offset);
    }

    /**
     * Vai para o próximo token.
     * 
     * @return O úlitimo (Se ele nao puder avançar retorna o EOF).
     */
    protected Token advance() {
        if (isAtEnd()) return peek();
        current++;
        return previous();
    }

    /**
     * 
     * @return O último token.
     */
    protected Token previous() {
        return tokens.get(current - 1);
    }

    /**
     * 
     * @param type TokeType do token a ser consumido.
     * @param errMessage Mensagem de erro.
     * @return O token consumido.
     */
    protected Token consume(TokenType type, String errMessage) {
        if (checkTokenType(type, peek())) return advance();
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
    protected ParseError error(Token token, String errMessage) {
        throw new ParseError("Erro em linha: " + token.getTokenLine() + ":" + token.getTokenCaracter() + ": " + errMessage);
    }

    /**
     * Avança para o próximo comando válido.
     */
    protected void synchronize() {
        advance();

        while (!isAtEnd()) {
            if (previous().getTokenType() == TokenType.SEMI_COLUMN) return;

            switch (peek().getTokenType()) {
                case TokenType.FOR:
                case TokenType.IF:
                case TokenType.PRINT:
                case TokenType.SET:
                case TokenType.WHILE:
                case TokenType.IDENTIFIER:
                case TokenType.LEFT_BRACE:
                    return;
                default:
                    advance();
            }
        }
    }
}