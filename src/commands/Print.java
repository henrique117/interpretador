package commands;

import interfaces.ICommand;
import interfaces.IArgumentExtractor;
import interpreter.Variable;
import interpreter.VariableStore;

import java.util.List;
import java.util.regex.Matcher;

public class Print implements ICommand, IArgumentExtractor {

    @Override
    public List<String> extract(Matcher matcher) {
        return List.of(matcher.group(1).split(","));
    }

    @Override
    public boolean execute(List<String> args, VariableStore store) {
        StringBuilder output = new StringBuilder();

        for (String arg : args) {
            arg = arg.trim();

            if (arg.startsWith("\"") && arg.endsWith("\"") && arg.length() >= 2) {
                output.append(arg, 1, arg.length() - 1);
            } else if (arg.matches("[a-zA-Z_]\\w*")) {
                Variable variable = store.get(arg);

                if (variable == null) {
                    System.err.println("Error: Variable \"" + arg + "\" not found.");
                    return true;
                }

                output.append(variable.value);
            } else {
                System.err.println("Error: Invalid argument \"" + arg + "\"");
                return true;
            }
        }

        System.out.println(output);
        return false;
    }
}