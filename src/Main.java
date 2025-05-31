import java.util.List;

import lexer.Lexer;
import lexer.Token;

public class Main {
    public static void main(String[] args) {
        String program = FileReader.readFile("program");
        Lexer lexer = new Lexer(program);

        List<Token> tokens = lexer.tokenize();

        for (Token t : tokens) {
            System.out.println(t);
        }
    }
}