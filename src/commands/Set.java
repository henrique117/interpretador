package commands;

import interfaces.ICommand;
import interpreter.VariableStore;

import java.util.List;

public class Set implements ICommand {

    @Override
    public void execute(List<String> args, VariableStore variables) {
        String varName = args.getFirst();
        String rawValue = args.getLast();
    }
}
