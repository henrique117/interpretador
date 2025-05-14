package interpreter;

import java.util.List;

public class ParsedCommand {

    private final String name;
    private final List<String> args;
    private final String error;

    public ParsedCommand(String name, List<String> args) {
        this(name, args, null);
    }

    public ParsedCommand(String name, List<String> args, String error) {
        this.name = name;
        this.args = args;
        this.error = error;
    }

    public String getName() {
        return name;
    }

    public List<String> getArgs() {
        return args;
    }

    public boolean hasError() {
        return error != null && !error.isEmpty();
    }

    public String getError() {
        return error;
    }
}