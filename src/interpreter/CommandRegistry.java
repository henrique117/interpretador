package interpreter;

import commands.Print;
import commands.Set;
import interfaces.ICommand;
import interfaces.IArgumentExtractor;

import java.util.HashMap;
import java.util.regex.Pattern;

public class CommandRegistry {

    private static final HashMap<String, CommandDefinition> commands = new HashMap<>();

    static {
        register("set", "^set\\s+([a-zA-Z_]\\w*)\\s*=\\s*(.+)$", new Set(), new Set());
        register("print", "^print\\s*\\(\\s*(.+?)\\s*\\)$", new Print(), new Print());
    }

    private static void register(String name, String regex, ICommand command, IArgumentExtractor extractor) {
        Pattern pattern = Pattern.compile(regex);
        CommandDefinition def = new CommandDefinition(name, pattern, extractor, command);
        commands.put(name, def);
    }

    public static CommandDefinition getCommandDefinition(String name) {
        return commands.get(name);
    }
}