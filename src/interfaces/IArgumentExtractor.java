package interfaces;

import java.util.List;
import java.util.regex.Matcher;

public interface IArgumentExtractor {
    List<String> extract(Matcher matcher);
}