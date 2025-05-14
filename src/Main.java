import interpreter.CommandDefinition;
import interpreter.CommandRegistry;
import interpreter.ParsedCommand;
import interpreter.VariableStore;
import parser.Parser;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        VariableStore store = new VariableStore();

        List<String> program = FileReader.readFile("program");

        for (String line : program) {
            line = line.trim();

            if (line.isEmpty() || line.startsWith("#")) continue;

            ParsedCommand parsedCommand = Parser.parse(line);

            if (parsedCommand.hasError()) {
                System.err.println("Erro: " + parsedCommand.getError());
                break;
            }

            CommandDefinition commandDefinition = CommandRegistry.getCommandDefinition(parsedCommand.getName());

            if (commandDefinition == null) {
                System.err.println("Erro: O comando \"" + parsedCommand.getName() + "\" não existe");
                break;
            }

            commandDefinition.getCommand().execute(parsedCommand.getArgs(), store);
        }
    }
}