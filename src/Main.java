import java.util.List;

import debug.TokensPrinter;
import lexer.Lexer;
import lexer.Token;
import parser.StmtParser;
import parser.interfaces.Stmt;

public class Main {
    public static void main(String[] args) {
        String program = FileReader.readFile("program");

        Lexer lexer = new Lexer(program);

        List<Token> tokens = lexer.tokenize();

        TokensPrinter tokensPrinter = new TokensPrinter(tokens);

        System.out.println(tokensPrinter.print());

        StmtParser parser = new StmtParser(tokens);

        List<Stmt> statements = parser.parse();

        System.out.println(statements.size());
    }
}