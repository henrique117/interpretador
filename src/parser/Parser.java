package parser;

import interpreter.CommandDefinition;
import interpreter.CommandRegistry;
import interpreter.ParsedCommand;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;

public class Parser {

    public static ParsedCommand parse(String line) {
        String firstWord = line.split("\\s+|\\(")[0];
        CommandDefinition commandDefinition = CommandRegistry.getCommandDefinition(firstWord);

        if (commandDefinition == null) return new ParsedCommand(firstWord, Collections.emptyList(), "Comando \"" + firstWord + "\" não encontrado");

        Matcher matcher = commandDefinition.getPattern().matcher(line);

        if (!matcher.matches()) return new ParsedCommand(firstWord, Collections.emptyList(), "Comando \"" + firstWord + "\" não executado corretamente");

        List<String> args = commandDefinition.getExtractor().extract(matcher);

        return new ParsedCommand(firstWord, args);
    }
}