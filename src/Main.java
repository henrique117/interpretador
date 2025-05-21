import interpreter.CommandDefinition;
import interpreter.CommandRegistry;
import interpreter.ParsedCommand;
import interpreter.VariableStore;
import parser.Parser;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        VariableStore store = new VariableStore();
        int lineNumber = 1;
        List<String> program = FileReader.readFile("program");

        for (String line : program) {
            line = line.trim();

            if (line.isEmpty() || line.startsWith("#")) {
                lineNumber++;
                continue;
            }

            ParsedCommand parsedCommand = Parser.parse(line, store);

            if (parsedCommand.hasError()) {
                System.err.println("Error on line " + lineNumber + ": " + parsedCommand.getError());
                break;
            }

            CommandDefinition commandDefinition = CommandRegistry.getCommandDefinition(parsedCommand.getName());

            if (commandDefinition == null) {
                System.err.println("Error on line " + lineNumber + ": The command \"" + parsedCommand.getName() + "\" doesn't exist");
                break;
            }

            String err = commandDefinition.getCommand().execute(parsedCommand.getArgs(), store);

            if (err != null) {
                System.err.println("Error on line " + lineNumber + ": " + err);
                break;
            }

            lineNumber++;
        }
    }
}