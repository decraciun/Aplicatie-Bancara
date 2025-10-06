import java.util.Objects;

public class Client {
    private final String nume;
    private final String CNP;
    private Adresa adresa;
    private String email;
    private String telefon;
    private String parola;

    public Client(String nume, String cnp, Adresa adresa, String email, String telefon, String parola) {
        this.nume = nume;
        this.CNP = cnp;
        this.email = email;
        this.telefon = telefon;
        this.adresa = adresa;
        this.parola = parola;
    }

    public String getNume() {
        return this.nume;
    }

    public String getCnp() {
        return this.CNP;
    }

    public String getEmail() {
        return this.email;
    }

    public String getParola() {
        return parola;
    }

    public String getTelefon() {
        return this.telefon;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public Adresa getAdresa() { return this.adresa; }

    @Override
    public String toString() {
        return nume + " (CNP: " + CNP + ") " + "ADRESA: " + adresa + " E-MAIL: " + email + " TELEFON: " + telefon;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(CNP, client.CNP);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(CNP);
    }
}
