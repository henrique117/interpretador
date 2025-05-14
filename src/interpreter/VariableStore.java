package interpreter;

import java.util.HashMap;

public class VariableStore {

    private final HashMap<String, Variable> variables;

    public VariableStore() {
        variables = new HashMap<>();
    }

    public void set(String name, Variable value) {
        variables.put(name, value);
    }

    public Variable get(String name) {
        return variables.get(name);
    }

    public boolean exists(String name) {
        return variables.containsKey(name);
    }

    public Variable.Type getType(String name) {
        Variable var = variables.get(name);
        return var != null ? var.type : null;
    }

    public Object getValue(String name) {
        Variable var = variables.get(name);
        return var != null ? var.value : null;
    }
}