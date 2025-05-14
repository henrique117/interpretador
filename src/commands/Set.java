package commands;

import interfaces.IArgumentExtractor;
import interfaces.ICommand;
import interpreter.Variable;
import interpreter.VariableStore;

import java.util.List;
import java.util.regex.Matcher;

public class Set implements ICommand, IArgumentExtractor {

    @Override
    public List<String> extract(Matcher matcher) {
        return List.of(matcher.group(1), matcher.group(2));
    }

    @Override
    public boolean execute(List<String> args, VariableStore store) {
        String varName = args.getFirst();
        String rawValue = args.getLast();

        Variable variable = Variable.fromString(rawValue);
        store.set(varName, variable);

        return false;
    }
}