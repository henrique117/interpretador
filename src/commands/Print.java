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
        return List.of(matcher.group(1));
    }

    @Override
    public void execute(List<String> args, VariableStore store) {
        String varName = args.getFirst();
        Variable value = store.get(varName);

        if (value != null) {
            System.out.println(value.value);
        } else {
            System.out.println("Variable not found: " + varName);
        }
    }
}