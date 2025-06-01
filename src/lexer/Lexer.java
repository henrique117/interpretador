package lexer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lexer {

    private final String source;                    // Texto inteiro do código
    private final List<Token> tokens;               // Tokens gerados pelo Lexer
    private final Map<String, TokenType> keywords;  // Mapa de Keywords

    private int line = 1;       // Linha do código
    private int caracter = 1;   // Caracter da linha
    private int current = 0;    // Posição da cabeça de leitura no texto

    public Lexer(String source) {
        this.source = source;

        tokens = new ArrayList<>();
        keywords = new HashMap<>();

        keywords.put("if", TokenType.IF);
        keywords.put("else", TokenType.ELSE);
        keywords.put("while", TokenType.WHILE);
        keywords.put("for", TokenType.FOR);
        keywords.put("set", TokenType.SET);
        keywords.put("print", TokenType.PRINT);
        keywords.put("true", TokenType.TRUE);
        keywords.put("false", TokenType.FALSE);
        keywords.put("and", TokenType.AND);
        keywords.put("or", TokenType.OR);
    }

    public List<Token> tokenize() {

        while (!isAtEnd()) {
            scanToken();
        }

        addToken(TokenType.EOF, "eof", caracter);

        return tokens;
    }

    private void scanToken() {
        char c = advance();

        switch (c) {
            case '(': addToken(TokenType.LEFT_PAREN); break;
            case ')': addToken(TokenType.RIGHT_PAREN); break;
            case '{': addToken(TokenType.LEFT_BRACE); break;
            case '}': addToken(TokenType.RIGHT_BRACE); break;
            case '[': addToken(TokenType.LEFT_BRACKET); break;
            case ']': addToken(TokenType.RIGHT_BRACKET); break;
            case ',': addToken(TokenType.COMMA); break;
            case '/': addToken(TokenType.DIV); break;
            case '%': addToken(TokenType.MODULE); break;
            case ' ': break;
            case '\r': break;
            case '\t': break;

            case '\n':
                addToken(TokenType.NEW_LINE, "\\n");
                line++;
                caracter = 1;
                break;

            case '\'':
                addToken(TokenType.SINGLE_QUOTES);
                string(c);
                break;

            case '"':
                addToken(TokenType.DOUBLE_QUOTES);
                string(c);
                break;
        
            case '=':
                if (match('=')) {
                    addToken(TokenType.EQUAL_EQUAL, "==");
                } else {
                    addToken(TokenType.EQUAL);
                }
                break;
                
            case '!':
                if (match('=')) {
                    addToken(TokenType.NOT_EQUAL, "!=");
                } else {
                    addToken(TokenType.NOT, "!");
                }
                break;

            case '>':
                if (match('=')) {
                    addToken(TokenType.GREATER_EQUAL, ">=");
                } else {
                    addToken(TokenType.GREATER, ">");
                }
                break;

            case '<':
                if (match('=')) {
                    addToken(TokenType.LESS_EQUAL, "<=");
                } else {
                    addToken(TokenType.LESS, "<");
                }
                break;

            case '*':
                if (match('*')) {
                    addToken(TokenType.POWER_OF, "**");
                } else {
                    addToken(TokenType.MULT, "*");
                }
                break;

            case '+':
                if (match('+')) {
                    addToken(TokenType.PLUS_PLUS, "++");
                } else {
                    addToken(TokenType.PLUS, "+");
                }
                break;

            case '-':
                if (match('-')) {
                    addToken(TokenType.MINUS_MINUS, "--");
                } else {
                    addToken(TokenType.MINUS, "-");
                }
                break;

            case '&':
                if (match('&')) addToken(TokenType.AND, "&&");
                break;

            case '|':
                if (match('|')) addToken(TokenType.OR, "||");
                break;
        
            default:
                if (isAlpha(c)) identifier(c);
                if (Character.isDigit(c)) number(c);
                break;
        }
    }

    // Helpers

    // Métodos Booleanos

    /**
     * 
     * @param expected Caracter esperado
     * @return Se é ou não aquele caracter
     */
    private boolean match(char expected) {
        if (isAtEnd() || peek() != expected) return false;

        advance();
        return true;
    }

    /**
     * 
     * @return Se está no final da string
     */
    private boolean isAtEnd() {
        return current >= source.length();
    }

    /**
     * 
     * @param c Caracter a ser checado
     * @return Se é uma letra
     */
    private boolean isAlpha(char c) {
        return Character.isLetter(c) || c == '_';
    }

    /**
     * 
     * @param c Caracter a ser checado
     * @return Se é letra ou número
     */
    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || Character.isDigit(c);
    }

    // Métodos Char

    /**
     * 
     * @return Retorna o caracter atual e passa para o próximo
     */
    private char advance() {
        char c = source.charAt(current);
        current++;
        caracter++;
        return c;
    }

    /**
     * 
     * @return Retorna o caracter atual
     */
    private char peek() {
        return peek(0);
    }

    /**
     * 
     * @param offset Quantas posições a frente você quer ler
     * @return Retorna o caracter daquela posição
     */
    private char peek(int offset) {
        if (current + offset >= source.length()) {
            return '\0';
        }
        return source.charAt(current + offset);
    }

    // Métodos Void

    /**
     * Adiciona um token a lista com o valor da posição atual
     * 
     * @param type Tipo do token listado no TokenType enum
     */
    private void addToken(TokenType type) {
        addToken(type, String.valueOf(source.charAt(current - 1)));
    }

    /**
     * Adiciona um token a lista
     * 
     * @param type Tipo do token listado no TokenType enum
     * @param value Valor do lexema
     */
    private void addToken(TokenType type, String value) {
        tokens.add(new Token(type, value, line, caracter - 1));
    }

    private void addToken(TokenType type, String value, int caracter) {
        tokens.add(new Token(type, value, line, caracter));
    }

    /**
     * Builda a palavra e adiciona o token de ID ou Keyword
     * 
     * @param firstChar Primeiro caracter pra buildar a string
     */
    private void identifier(char firstChar) {
        StringBuilder builder = new StringBuilder();
        int startColumn = caracter - 1;

        builder.append(firstChar);

        while (!isAtEnd() && isAlphaNumeric(peek())) {
            builder.append(advance());
        }

        String text = builder.toString();

        TokenType type = keywords.getOrDefault(text, TokenType.IDENTIFIER);
        addToken(type, text, startColumn);
    }

    /**
     * Builda um número
     * 
     * @param firstNumber Primeiro caracter pra buildar uma string de um número
     */
    private void number(char firstNumber) {
        StringBuilder builder = new StringBuilder();
        int startColumn = caracter - 1;
        
        builder.append(firstNumber);

        while (!isAtEnd() && Character.isDigit(peek())) {
            builder.append(advance());
        }

        addToken(TokenType.NUMBER, builder.toString(), startColumn);
    }

    /**
     * Builda uma String
     * 
     * @param quoteType Tipo das aspas usado
     */
    private void string(char quoteType) {
        StringBuilder builder = new StringBuilder();
        int startColumn = caracter - 1;

        while (!isAtEnd() && peek() != quoteType) {
            char c = advance();

            if(c == '\n') {
                line++;
                caracter = 1;
            }

            builder.append(c);
        }

        if (isAtEnd()) {
            System.err.println("Erro: string não terminada na linha " + line);
            return;
        }

        advance();

        addToken(TokenType.STRING, builder.toString(), startColumn);

        if (quoteType == '"') {
            addToken(TokenType.DOUBLE_QUOTES);
        } else {
            addToken(TokenType.SINGLE_QUOTES);
        }
    }
}