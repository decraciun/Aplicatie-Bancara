import java.time.LocalDateTime;

public class SesiuneAutentificare {
    private final Client client;
    private final LocalDateTime data;
    private boolean activa;

    private SesiuneAutentificare(Client client) {
        this.client = client;
        this.data = LocalDateTime.now();
        this.activa = true;
    }

    public static SesiuneAutentificare autentificare(Client client, String email, String parola) {
        if (client == null) {
            System.out.println("Autentificarea a esuat! Email-ul sau parola sunt gresite!");
            return null;
        }
        if (client.getEmail().equals(email) && ServiciuParole.verificaParola(parola, client.getParola())) {
            System.out.println("Te-ai autentificat cu succes!");
            return new SesiuneAutentificare(client);
        } else {
            System.out.println("Autentificarea a esuat! Email-ul sau parola sunt gresite!");
            return null;
        }
    }

    public void deconectare() {
        this.activa = false;
    }

    public boolean esteActiva() {
        return activa;
    }

    public Client getClient() {
        return client;
    }

    public LocalDateTime getData() {
        return data;
    }
}
