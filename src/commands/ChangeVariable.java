package commands;

import java.util.List;
import java.util.regex.Matcher;

import interfaces.IArgumentExtractor;
import interfaces.ICommand;
import interpreter.Variable;
import interpreter.VariableStore;

public class ChangeVariable implements ICommand, IArgumentExtractor{

    @Override
    public List<String> extract(Matcher matcher) {
        return List.of(matcher.group(1), matcher.group(2));
    }

    @Override
    public boolean execute(List<String> args, VariableStore variables) {

        if (!variables.exists(args.get(0))) {
            System.err.println("Variable \"" + args.get(0) + "\" does not exists");
            return true;
        }

        variables.set(args.get(0), Variable.fromString(args.get(1)));
        return false;
    }
}