import java.util.Objects;

public class ContBancar {
    private final String IBAN;
    private final Client client;
    private double sold;

    public ContBancar(String IBAN, Client client) {
        this.IBAN = IBAN;
        this.client = client;
        this.sold = 0.0;
    }

    public String getIBAN() { return IBAN; }

    public Client getClient() { return client; }

    public double getSold() { return sold; }

    public void setSold(double sold) {
        this.sold = sold;
    }

    @Override
    public String toString() {
        return client.getNume() + " (CNP: " + client.getCnp() + ")\n" + "IBAN: " +IBAN + "\n" + "SOLD: " + sold + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ContBancar that = (ContBancar) o;
        return Objects.equals(IBAN, that.IBAN) && Objects.equals(client, that.client);
    }

    @Override
    public int hashCode() {
        return Objects.hash(IBAN, client);
    }
}
