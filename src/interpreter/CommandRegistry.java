package interpreter;

import interfaces.ICommand;

import java.util.HashMap;
import java.util.List;

public class CommandRegistry {

    private static final HashMap<String, CommandDefinition> commands = new HashMap<>();

    public static void register(CommandDefinition command) {
        commands.put(command.getName(), command);
    }

    public static void executeCommand(ParsedCommand parsedCommand, VariableStore variables) {
        CommandDefinition commandDefinition = commands.get(parsedCommand.getName());

        if (commandDefinition == null) {
            System.out.println("Erro: Comando \"" + parsedCommand.getName() + "\" não encontrado");
            return;
        }

        ICommand command = commandDefinition.getCommand();
        List<String> args = parsedCommand.getArgs();

        command.execute(args, variables);
    }

    public static CommandDefinition getCommandDefinition(String name) {
        return commands.get(name);
    }
}