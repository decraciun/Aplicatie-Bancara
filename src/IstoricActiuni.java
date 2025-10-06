import java.time.LocalDateTime;

public class IstoricActiuni {
    private String descriere;
    private LocalDateTime data;

    public IstoricActiuni(String descriere) {
        this.descriere = descriere;
        this.data = LocalDateTime.now();
    }

    public String getDescriere() { return descriere; }

    public LocalDateTime getData() { return data; }

    @Override
    public String toString() {
        return "[" + data + "] " + descriere;
    }
}
