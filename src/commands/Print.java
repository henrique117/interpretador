package commands;

import interfaces.ICommand;
import interfaces.IArgumentExtractor;
import interpreter.Variable;
import interpreter.VariableStore;
import utils.Errors;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Print implements ICommand, IArgumentExtractor {

    @Override
    public List<String> extract(Matcher matcher) {
        String rawArgs = matcher.group(1);
        List<String> args = new ArrayList<>();

        Matcher argMatcher = Pattern.compile("(\"[^\"]*\")|([^,\\s]+)").matcher(rawArgs);
        while (argMatcher.find()) {
            if (argMatcher.group(1) != null) {
                args.add(argMatcher.group(1));
            } else {
                args.add(argMatcher.group(2));
            }
        }

        return args;
    }


    @Override
    public String execute(List<String> args, VariableStore store) {
        StringBuilder output = new StringBuilder();

        for (String arg : args) {
            arg = arg.trim();

            if (arg.startsWith("\"") && arg.endsWith("\"") && arg.length() >= 2) {
                output.append(arg, 1, arg.length() - 1);
            } else if (arg.matches("[a-zA-Z_]\\w*")) {
                Variable variable = store.get(arg);

                if (variable == null) {
                    return Errors.variableNotFound(arg);
                }

                output.append(variable.value);
            } else {
                return "Invalid argument \"" + arg + "\"";
            }
        }

        System.out.println(output);
        return null;
    }
}