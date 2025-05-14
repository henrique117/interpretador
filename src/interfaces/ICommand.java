package interfaces;

import java.util.List;

import interpreter.VariableStore;

@FunctionalInterface
public interface ICommand {
    void execute(List<String> args, VariableStore variables);
}