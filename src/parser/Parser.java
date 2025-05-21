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
import utils.Result;

public class Parser {

    public static ParsedCommand parse(String line, VariableStore store) {
        String firstWord = line.split("\\s+|\\(")[0];
        CommandDefinition commandDefinition = CommandRegistry.getCommandDefinition(firstWord);

        if (commandDefinition == null && !store.exists(firstWord))
            return new ParsedCommand(firstWord, Collections.emptyList(), "Command \"" + firstWord + "\" not found");

        if (commandDefinition == null && store.exists(firstWord))
            commandDefinition = CommandRegistry.getCommandDefinition("changevar");

        assert commandDefinition != null;
        Matcher matcher = commandDefinition.getPattern().matcher(line);

        if (!matcher.matches())
            return new ParsedCommand(firstWord, Collections.emptyList(), "The command \"" + firstWord + "\" is not used properly");

        List<String> args = new ArrayList<>(commandDefinition.getExtractor().extract(matcher));

        if ((firstWord.equals("set") && args.size() == 2) || (commandDefinition.getName().equals("changevar") && args.size() == 2)) {
            Result<List<String>> result = parseBinExpr(args, store);
            if (!result.isOk()) {
                return new ParsedCommand(firstWord, args, result.getError());
            }
            args = result.getValue();
        }

        return new ParsedCommand(commandDefinition.getName(), args);
    }

    private static Result<List<String>> parseBinExpr(List<String> args, VariableStore variableStore) {
        List<String> exprTokens = Lexer.tokenize(args.get(1));
        BinaryExprParser binParser = new BinaryExprParser(exprTokens, variableStore);

        Result<IExpr> exprResult = binParser.parse();
        if (!exprResult.isOk()) return Result.error(exprResult.getError());

        Result<Object> evalResult = exprResult.getValue().evaluate();
        if (!evalResult.isOk()) return Result.error(evalResult.getError());

        Object value = evalResult.getValue();
        String evaluatedValue;

        if (value instanceof Number num) {
            double val = num.doubleValue();
            evaluatedValue = val % 1 == 0 ? Long.toString(Math.round(val)) : Double.toString(val);
        } else {
            evaluatedValue = value.toString();
        }

        args.set(1, evaluatedValue);
        return Result.ok(args);
    }
}
