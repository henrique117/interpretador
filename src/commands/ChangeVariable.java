package commands;

import java.util.List;
import java.util.regex.Matcher;

import interfaces.IArgumentExtractor;
import interfaces.ICommand;
import interpreter.Variable;
import interpreter.VariableStore;
import utils.Errors;

public class ChangeVariable implements ICommand, IArgumentExtractor {

    @Override
    public List<String> extract(Matcher matcher) {
        return List.of(matcher.group(1), matcher.group(2));
    }

    @Override
    public String execute(List<String> args, VariableStore variables) {
        if (args.size() != 2) {
            return "ChangeVariable command expects 2 arguments.";
        }

        String varName = args.get(0);
        String newValue = args.get(1);

        if (!variables.exists(varName)) {
            return Errors.variableNotFound(varName);
        }

        variables.set(varName, Variable.fromString(newValue));
        return null;
    }
}
