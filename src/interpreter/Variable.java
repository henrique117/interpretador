package interpreter;

public class Variable {

    public enum Type {
        INT, FLOAT, STRING, BOOLEAN
    }

    public Type type;
    public Object value;

    public Variable(Type type, Object value) {
        this.type = type;
        this.value = value;
    }

    public static Variable fromString(String raw) {
        if (raw.matches("-?\\d+")) return new Variable(Type.INT, Integer.parseInt(raw));

        if (raw.matches("-?\\d+\\.\\d+")) return new Variable(Type.FLOAT, Float.parseFloat(raw));

        if (raw.startsWith("\"") && raw.endsWith("\"")) return new Variable(Type.STRING, raw.substring(1, raw.length() - 1));

        if (raw.equalsIgnoreCase("true") || raw.equalsIgnoreCase("false")) return new Variable(Type.BOOLEAN, Boolean.parseBoolean(raw));

        return new Variable(Type.STRING, raw);
    }
}