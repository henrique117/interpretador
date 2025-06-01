import java.util.List;

import debug.AstPrinter;
import debug.TokensPrinter;
import lexer.Lexer;
import lexer.Token;
import parser.ExprParser;

public class Main {
    public static void main(String[] args) {
        String program = FileReader.readFile("program");
        Lexer lexer = new Lexer(program);

        List<Token> tokens = lexer.tokenize();

        TokensPrinter tokensPrinter = new TokensPrinter(tokens);

        System.out.println(tokensPrinter.print());

        ExprParser exprParser = new ExprParser(tokens);

        AstPrinter astPrinter = new AstPrinter();

        System.out.println(astPrinter.print(exprParser.parse()));
    }
}