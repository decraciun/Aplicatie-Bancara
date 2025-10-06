import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServiciuAudit {
    private static final String FILE_PATH = "audit.csv";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void scrieActiune(String numeActiune) {
        String timestamp = LocalDateTime.now().format(formatter);
        String linie = numeActiune + "," + timestamp + "\n";

        try (FileWriter fw = new FileWriter(FILE_PATH, true)) {
            fw.write(linie);
        } catch (IOException e) {
            System.err.println("Eroare la scrierea in fisierul de audit: " + e.getMessage());
        }
    }
}
