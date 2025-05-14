package interfaces;

import java.util.List;

import interpreter.VariableStore;

@FunctionalInterface
public interface ICommand {
    boolean execute(List<String> args, VariableStore variables);
}