import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ServiciuBancar {
    private static ServiciuBancar instance;

    private final Map<String, Client> clienti;
    private final List<ContBancar> conturi;
    private final Set<Tranzactie> tranzactii;
    private final List<IstoricActiuni> istoricActiuni;
    private static final String prefix = "RO99AAAA";
    private final List<Card> carduri;

    private final ClientService clientService;
    private final ContBancarService contBancarService;
    private final TranzactieService tranzactieService;
    private final CardService cardService;

    private SesiuneAutentificare sesiuneCurenta = null;

    public Map<String, Client> listToMap(List<Client> clientiLista) {
        Map<String, Client> clientiMap = new HashMap<>();
        for (Client client : clientiLista) {
            clientiMap.put(client.getCnp(), client);
        }
        return clientiMap;
    }

    private ServiciuBancar() throws SQLException {
        this.clientService = ClientService.getInstance();
        this.contBancarService = ContBancarService.getInstance();
        this.tranzactieService = TranzactieService.getInstance();
        this.cardService = CardService.getInstance();
        this.clienti = listToMap(clientService.readAll());
        this.conturi = contBancarService.readAll();
        this.tranzactii = new TreeSet<>(tranzactieService.readAll());
        this.istoricActiuni = new ArrayList<>();
        this.carduri = cardService.readAll();
    }

    public void detaliiClient(Client client) {
        System.out.println("\n--- DETALII CLIENT ---");
        System.out.println("Nume: " + client.getNume());
        System.out.println("CNP: " + client.getCnp());
        System.out.println("Email: " + client.getEmail());
        System.out.println("Telefon: " + client.getTelefon());
        System.out.println("Adresa: " + client.getAdresa());

        System.out.println("\n--- CONTURI BANCARE ---");
        int countConturi = 0;
        for (ContBancar cont : conturi) {
            if (cont.getClient().equals(client)) {
                countConturi++;
                System.out.println(countConturi + ". IBAN: " + cont.getIBAN() + " | Sold: " + cont.getSold() + " RON");
            }
        }
        if (countConturi == 0) {
            System.out.println("Clientul nu are conturi bancare.");
        }

        System.out.println("\n--- CARDURI ---");
        int countCarduri = 0;
        for (Card card : carduri) {
            if (card.getCont().getClient().equals(client)) {
                countCarduri++;
                System.out.println(countCarduri + ". " + card);
            }
        }
        if (countCarduri == 0) {
            System.out.println("Clientul nu are carduri.");
        }
        ServiciuAudit.scrieActiune("Afisare detalii cont");
    }

    public Client autentificare(Scanner sc) {
        System.out.print("Introdu email: ");
        String email = sc.nextLine();

        System.out.print("Introdu parola: ");
        String parola = sc.nextLine();

        Client client = clientService.read(email);

        SesiuneAutentificare sesiune = SesiuneAutentificare.autentificare(client, email, parola);
        if (sesiune != null) {
            sesiuneCurenta = sesiune;
            istoricActiuni.add(new IstoricActiuni("S-a autentificat clientul cu email-ul " + email));
            ServiciuAudit.scrieActiune("Autentificare client");
            return client;
        }
        return null;
    }

    public void setSesiuneCurenta() {
        this.sesiuneCurenta = null;
    }

    public static synchronized ServiciuBancar getInstance() throws SQLException {
        if (instance == null) {
            instance = new ServiciuBancar();
        }
        return instance;
    }

    public String genereazaIBAN() {
        Random random = new Random();
        String iban;
        do {
        StringBuilder cifre = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            cifre.append(random.nextInt(10));
        }
        iban = prefix + cifre;
        } while (verificareIBAN(iban));
        return iban;
    }

    public String genereazaNumarCard() {
        Random rand = new Random();
        StringBuilder numar = new StringBuilder("9");
        for (int i = 0; i < 15; i++) {
            numar.append(rand.nextInt(10));
        }
        return numar.toString();
    }

    public String genereazaDataExpirare() {
        LocalDate data = LocalDate.now().plusYears(4);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/yy");
        return data.format(format);
    }

    public String genereazaCVV() {
        Random rand = new Random();
        int cvv = rand.nextInt(900) + 100;
        return String.valueOf(cvv);
    }

    public boolean verificareIBAN(String iban) {
        for (ContBancar cont : conturi) {
            if (cont.getIBAN().equals(iban)) return true;
        }
        return false;
    }

    public static boolean esteCNPValid(String cnp) {
        if (cnp == null || cnp.length() != 13) {
            return false;
        }

        if (!cnp.matches("\\d{13}")) {
            return false;
        }

        char primaCifra = cnp.charAt(0);
        return primaCifra == '1' || primaCifra == '2' || primaCifra == '5' || primaCifra == '6';
    }


    public void adaugaClient(Scanner sc) {
        System.out.println("\tAdauga client (Pentru a lasa un camp necompletat doar se apasa ENTER):");
        System.out.print("Nume: ");
        String nume = sc.nextLine();
        System.out.print("CNP: ");
        String cnp = sc.nextLine();
        if (!esteCNPValid(cnp)) {
            System.out.println("CNP-ul nu este valid!");
            return;
        }
        System.out.print("Judet: ");
        String judet = sc.nextLine();
        System.out.print("Oras: ");
        String oras = sc.nextLine();
        System.out.print("Strada: ");
        String strada = sc.nextLine();
        System.out.print("Numar: ");
        String numar = sc.nextLine();
        System.out.print("Bloc: ");
        String bloc = sc.nextLine();
        System.out.print("Scara: ");
        String scara = sc.nextLine();
        System.out.print("Apartament: ");
        String apartament = sc.nextLine();
        System.out.print("E-mail: ");
        String email = sc.nextLine();
        System.out.print("Telefon: ");
        String telefon = sc.nextLine();
        System.out.print("Parola: ");
        String parola = sc.nextLine();

        String parolaHashed = ServiciuParole.cripteazaParola(parola);

        Adresa adresa = (bloc.isEmpty() && scara.isEmpty() && apartament.isEmpty()) ?
                new Adresa(judet, oras, strada, numar) :
                new Adresa(judet, oras, strada, numar, bloc, scara, apartament);

        Client client = new Client(nume, cnp, adresa, email, telefon, parolaHashed);
        clienti.put(cnp, client);
        clientService.create(client);
        System.out.println("Clientul a fost adaugat cu succes!");
        istoricActiuni.add(new IstoricActiuni("A fost adaugat clientul cu CNP-ul " + client.getCnp()));
        ServiciuAudit.scrieActiune("Adaugare client");
    }

    public void adaugaCard(Scanner sc) {
        if (conturi.isEmpty()) {
            System.out.println("Nu exista conturi bancare!");
            return;
        }

        System.out.println("Selecteaza contul pentru care vrei sa adaugi un card:");
        for (int i = 0; i < conturi.size(); i++) {
            System.out.println((i + 1) + ". IBAN: " + conturi.get(i).getIBAN() + " | Client: " + conturi.get(i).getClient().getNume());
        }

        System.out.print("Introdu numarul contului: ");
        int numarCont = sc.nextInt();
        sc.nextLine();

        if (numarCont < 1 || numarCont > conturi.size()) {
            System.out.println("Numar invalid!");
            return;
        }

        ContBancar contBancar = conturi.get(numarCont - 1);
        System.out.print("Alege tipul cardului (1 - Debit, 2 - Credit): ");
        int tipCard = sc.nextInt();
        sc.nextLine();

        String numarCard = genereazaNumarCard();
        String dataExpirare = genereazaDataExpirare();
        String CVV = genereazaCVV();

        Card card;
        if (tipCard == 1) {
            card = new CardDebit(numarCard, dataExpirare, CVV, contBancar);
        } else if (tipCard == 2) {
            double limitaCard = 10000;
            card = new CardCredit(numarCard, dataExpirare, CVV, contBancar, limitaCard);
        } else {
            System.out.println("Tip de card invalid!");
            return;
        }

        carduri.add(card);
        cardService.create(card);
        System.out.println("Cardul a fost adaugat cu succes!");
        istoricActiuni.add(new IstoricActiuni("A fost adaugat un card pentru contul bancar cu IBAN-ul " + contBancar.getIBAN()));
        ServiciuAudit.scrieActiune("Adaugare card");
    }

    public void stergeCardClient(Scanner sc, Client client) {
        List<Card> carduriClient = carduri.stream()
                .filter(c -> c.getCont().getClient().equals(client))
                .toList();

        if (carduriClient.isEmpty()) {
            System.out.println("Clientul nu are carduri.");
            return;
        }

        System.out.println("Cardurile clientului:");
        for (Card c : carduriClient) {
            System.out.println("-> " + c.getNumarCard());
        }

        System.out.print("Introdu numarul cardului pe care vrei sa il stergi: ");
        String numar = sc.nextLine();

        Optional<Card> deSters = carduriClient.stream()
                .filter(c -> c.getNumarCard().equals(numar))
                .findFirst();

        if (deSters.isPresent()) {
            carduri.remove(deSters.get());
            cardService.delete(numar);
            System.out.println("Cardul a fost sters cu succes.");
        } else {
            System.out.println("Cardul nu a fost gasit.");
        }
        ServiciuAudit.scrieActiune("Stergere card");
    }


    public void afiseazaClienti() {
        int i = 0;
        for (Client client : clienti.values()) {
            i++;
            System.out.println(i + ". " + client);
        }
        ServiciuAudit.scrieActiune("Afisare clienti");
    }

    public void adaugaCont(Scanner sc) {
        if (clienti.isEmpty()) {
            System.out.println("Nu exista clienti inregistrati!");
            return;
        }

        System.out.print("Introdu CNP-ul clientului: ");
        String cnp = sc.nextLine();

        Client client = clientService.findByCnp(cnp);

        if (client == null) {
            System.out.println("Clientul cu CNP-ul specificat nu exista!");
            return;
        }

        String iban = genereazaIBAN();
        ContBancar cont = new ContBancar(iban, client);
        contBancarService.create(cont);
        this.conturi.add(cont);
        System.out.println("Contul a fost adaugat cu succes pentru clientul: " + client.getNume());
        istoricActiuni.add(new IstoricActiuni("A fost adaugat contul cu IBAN-ul " + cont.getIBAN()));
        ServiciuAudit.scrieActiune("Adaugare cont");
    }

    public void afiseazaConturi() {
        int i = 0;
        for (ContBancar cont: conturi) {
            i++;
            System.out.println(i + ". " + cont);
        }
        ServiciuAudit.scrieActiune("Afisare conturi");
    }

    public void adaugaTranzactie(Tranzactie tranzactie) {
        tranzactieService.create(tranzactie);
        tranzactii.add(tranzactie);
        istoricActiuni.add(new IstoricActiuni("Tranzactie: " + tranzactie.getTip() + " - " + tranzactie.getSuma() + " RON"));
        ServiciuAudit.scrieActiune("Adaugare tranzactie");
    }


    public void afiseazaTranzactiiClient(Scanner sc, Client client) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        System.out.print("Introdu data de inceput (format dd-MM-yyyy): ");
        String startStr = sc.nextLine();
        System.out.print("Introdu data de sfarsit (format dd-MM-yyyy): ");
        String endStr = sc.nextLine();

        LocalDateTime start, end;
        try {
            start = LocalDate.parse(startStr, formatter).atStartOfDay();
            end = LocalDate.parse(endStr, formatter).atTime(23, 59, 59);
        } catch (DateTimeParseException e) {
            System.out.println("Formatul datelor este invalid. Foloseste dd-MM-yyyy.");
            return;
        }

        List<ContBancar> conturiClient = conturi.stream()
                .filter(c -> c.getClient().equals(client))
                .toList();

        if (conturiClient.isEmpty()) {
            System.out.println("Clientul nu are conturi.");
            return;
        }

        System.out.println("Tranzactii in intervalul " + start.format(formatter) + " - " + end.format(formatter) + ":");

        List<Tranzactie> tranzactiiClient = tranzactii.stream().filter(t -> (conturiClient.contains(t.getSursa()) || conturiClient.contains(t.getDestinatie()))
                        && !t.getData().isBefore(start) && !t.getData().isAfter(end)).sorted().toList();

        if (tranzactiiClient.isEmpty()) {
            System.out.println("Nu exista tranzactii in acest interval.");
            return;
        }

        for (Tranzactie t : tranzactiiClient) {
            System.out.println("------------");
            System.out.println("Data: " + t.getData().format(formatter));
            System.out.println("Tip: " + t.getTip());
            System.out.println("Sursa: " + (t.getSursa() != null ? t.getSursa().getIBAN() : "-"));
            System.out.println("Destinatie: " + (t.getDestinatie() != null ? t.getDestinatie().getIBAN() : "-"));
            System.out.println("Suma: " + t.getSuma());
            System.out.println("Descriere: " + t.getDescriere());
        }
        ServiciuAudit.scrieActiune("Afisare tranzactii");
    }


    public void afiseazaTranzactiiClient(Scanner scanner) {
        System.out.print("Introduceti CNP-ul clientului: ");
        String cnp = scanner.nextLine();

        Client client = clienti.get(cnp);
        if (client == null) {
            System.out.println("Nu exista niciun client cu CNP-ul specificat.");
            return;
        }

        boolean tranzactiiGasite = false;
        int i = 0;
        for (Tranzactie tranzactie : tranzactii) {
            ContBancar sursa = tranzactie.getSursa();
            ContBancar destinatie = tranzactie.getDestinatie();

            boolean esteExpeditor = sursa != null && sursa.getClient().equals(client);
            boolean esteDestinatar = destinatie != null && destinatie.getClient().equals(client);
            if (esteExpeditor || esteDestinatar) {
                i++;
                System.out.println(i + ". Tranzactie: " + tranzactie.getTip() +
                        " | Suma: " + tranzactie.getSuma() +
                        " | Data: " + tranzactie.getData() +
                        " | Descriere: " + tranzactie.getDescriere());
                tranzactiiGasite = true;
            }
        }

        if (!tranzactiiGasite) {
            System.out.println("Clientul nu are tranzactii inregistrate.");
        }
        ServiciuAudit.scrieActiune("Afisare tranzactii");
    }

    public void plataNoua(Scanner sc, Client client) {
        List<ContBancar> conturiClient = conturi.stream().filter(c -> c.getClient().equals(client)).toList();

        if (conturiClient.isEmpty()) {
            System.out.println("Clientul nu are conturi bancare.");
            return;
        }

        System.out.println("Selecteaza contul pentru care doresti sa faci o plata:");
        for (int i = 0; i < conturiClient.size(); i++) {
            System.out.println((i + 1) + ". IBAN: " + conturiClient.get(i).getIBAN());
        }

        System.out.print("Alege un cont (numar): ");
        int indexCont;
        try {
            indexCont = Integer.parseInt(sc.nextLine()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Input invalid.");
            return;
        }

        if (indexCont < 0 || indexCont >= conturiClient.size()) {
            System.out.println("Index invalid.");
            return;
        }

        ContBancar contSelectat = conturiClient.get(indexCont);

        System.out.println("Selecteaza tipul platii:");
        System.out.println("1. Depunere");
        System.out.println("2. Extragere");
        System.out.println("3. Transfer");

        System.out.print("Alege optiunea: ");
        String optiune = sc.nextLine();
        String tip;
        ContBancar destinatie = null;
        String descriere = "";
        double suma;

        switch (optiune) {
            case "1":
                tip = "depunere";
                System.out.print("Introdu suma de depus: ");
                try {
                    suma = Double.parseDouble(sc.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Suma invalida.");
                    return;
                }
                descriere = "Depunere de fonduri";
                break;

            case "2":
                tip = "extragere";
                System.out.print("Introdu suma de extras: ");
                try {
                    suma = Double.parseDouble(sc.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Suma invalida.");
                    return;
                }
                if (suma > contSelectat.getSold()) {
                    System.out.println("Fonduri insuficiente!");
                    return;
                }
                descriere = "Extragere de fonduri";
                break;

            case "3":
                tip = "transfer";
                System.out.print("Introdu IBAN-ul contului destinatie: ");
                String ibanDest = sc.nextLine();

                Optional<ContBancar> contDest = conturi.stream()
                        .filter(c -> c.getIBAN().equals(ibanDest))
                        .findFirst();

                if (contDest.isEmpty()) {
                    System.out.println("Contul destinatie nu a fost gasit.");
                    return;
                }

                destinatie = contDest.get();
                System.out.print("Introdu suma de transferat: ");
                try {
                    suma = Double.parseDouble(sc.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Suma invalida.");
                    return;
                }

                if (suma > contSelectat.getSold()) {
                    System.out.println("Fonduri insuficiente!");
                    return;
                }

                System.out.print("Descriere tranzactie: ");
                descriere = sc.nextLine();
                break;

            default:
                System.out.println("Optiune invalida.");
                return;
        }

        Tranzactie tranzactie = new Tranzactie(contSelectat, destinatie, descriere, suma, tip);
        adaugaTranzactie(tranzactie);

        switch (tip) {
            case "depunere" -> {
                contSelectat.setSold(contSelectat.getSold() + suma);
                contBancarService.update(contSelectat);
            }
            case "extragere" -> {
                contSelectat.setSold(contSelectat.getSold() - suma);
                contBancarService.update(contSelectat);
            }
            case "transfer" -> {
                contSelectat.setSold(contSelectat.getSold() - suma);
                destinatie.setSold(destinatie.getSold() + suma);
                contBancarService.update(contSelectat);
                contBancarService.update(destinatie);
            }
        }

        System.out.println("Plata efectuata cu succes!");
        ServiciuAudit.scrieActiune("Efectuare plata");
    }

}
