import java.io.IOException;

public class FileReader {
    public static String readFile(String fileName) {
        try {

            java.nio.file.Path filePath = java.nio.file.Paths.get("./src/" + fileName + ".txt");
            return java.nio.file.Files.readString(filePath);

        } catch (IOException err) {

            System.out.println("Error on reading file: " + err.getMessage());
            return null;

        }
    }
}