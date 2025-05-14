package interpreter;

import java.util.regex.Pattern;

import interfaces.IArgumentExtractor;
import interfaces.ICommand;

public class CommandDefinition {

    private final String name;
    private final Pattern pattern;
    private final IArgumentExtractor extractor;
    private final ICommand command;

    public CommandDefinition(String name, Pattern pattern, IArgumentExtractor extractor, ICommand command) {
        this.name = name;
        this.pattern = pattern;
        this.extractor = extractor;
        this.command = command;
    }

    public String getName() {
        return name;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public IArgumentExtractor getExtractor() {
        return extractor;
    }

    public ICommand getCommand() {
        return command;
    }
}