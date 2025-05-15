package parser;

import interpreter.CommandDefinition;
import interpreter.CommandRegistry;
import interpreter.ParsedCommand;
import interpreter.VariableStore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;

import interfaces.IExpr;

public class Parser {

    public static ParsedCommand parse(String line, VariableStore store) {
        String firstWord = line.split("\\s+|\\(")[0];
        CommandDefinition commandDefinition = CommandRegistry.getCommandDefinition(firstWord);

        if (commandDefinition == null && !store.exists(firstWord))
            return new ParsedCommand(firstWord, Collections.emptyList(), "Comando \"" + firstWord + "\" não encontrado");

        if (commandDefinition == null && store.exists(firstWord)) 
            commandDefinition = CommandRegistry.getCommandDefinition("changevar");

        Matcher matcher = commandDefinition.getPattern().matcher(line);

        if (!matcher.matches())
            return new ParsedCommand(firstWord, Collections.emptyList(), "Comando \"" + firstWord + "\" não executado corretamente");

        List<String> args = new ArrayList<>(commandDefinition.getExtractor().extract(matcher));

        if ((firstWord.equals("set") && args.size() == 2) || (commandDefinition.getName().equals("changevar") && args.size() == 2)) {
            args = parseBinExpr(args);
        }

        return new ParsedCommand(commandDefinition.getName(), args);
    }

    private static List<String> parseBinExpr(List<String> args) {
        List<String> exprTokens = Lexer.tokenize(args.get(1));
        BinaryExprParser binParser = new BinaryExprParser(exprTokens);

        IExpr expr = binParser.parse();
        double evalResult = ((Number) expr.evaluate()).doubleValue();
        String evaluatedValue = evalResult % 1 == 0 ? Long.toString(Math.round(evalResult)) : Double.toString(evalResult);

        args.set(1, evaluatedValue);

        return args;
    }
}