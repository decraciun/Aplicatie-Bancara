import java.time.LocalDateTime;

public class Tranzactie implements Comparable<Tranzactie> {
    private int id;
    private final ContBancar sursa;
    private final ContBancar destinatie;
    private final String descriere;
    private final double suma;
    private final LocalDateTime data;
    private final String tip;

    public Tranzactie(int id, ContBancar sursa, ContBancar destinatie, String descriere, double suma, LocalDateTime data, String tip) {
        this.id = id;
        this.sursa = sursa;
        this.destinatie = destinatie;
        this.descriere = descriere;
        this.suma = suma;
        this.data = data;
        this.tip = tip;
    }

    public Tranzactie(ContBancar sursa, ContBancar destinatie, String descriere, double suma, String tip) {
        this(-1, sursa, destinatie, descriere, suma, LocalDateTime.now(), tip);
    }

    public int getId() { return id; }
    public ContBancar getSursa() { return sursa; }
    public ContBancar getDestinatie() { return destinatie; }
    public String getDescriere() { return descriere; }
    public double getSuma() { return suma; }
    public LocalDateTime getData() { return data; }
    public String getTip() { return tip; }

    @Override
    public int compareTo(Tranzactie o) {
        return this.data.compareTo(o.data);
    }

    public void setId(int id) {
        this.id = id;
    }
}
