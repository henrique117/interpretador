import java.io.IOException;
import java.util.List;

public class FileReader {
    public static List<String> readFile(String fileName) {
        try {

            java.nio.file.Path filePath = java.nio.file.Paths.get("./src/" + fileName + ".txt");
            return java.nio.file.Files.readAllLines(filePath);

        } catch (IOException err) {

            System.out.println("Error on reading file: " + err.getMessage());
            return java.util.Collections.emptyList();

        }
    }
}