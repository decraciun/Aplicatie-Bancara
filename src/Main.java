import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {
        ServiciuBancar serviciu = ServiciuBancar.getInstance();
        Scanner scanner = new Scanner(System.in);
        boolean ruleaza = true;
        Client clientAutentificat = null;

        while (ruleaza) {
            System.out.println("\n=== Meniu Principal ===");
            System.out.println("0. Iesire");
            System.out.println("1. Meniu banca");
            System.out.println("2. Meniu client");
            System.out.print("Alege o optiune: ");
            int optiune = scanner.nextInt();
            scanner.nextLine();

            switch (optiune) {
                case 0:
                    ruleaza = false;
                    break;

                case 1:
                    boolean meniuBanca = true;
                    while (meniuBanca) {
                        System.out.println("\n--- Meniu Banca ---");
                        System.out.println("0. Inapoi");
                        System.out.println("1. Afiseaza lista de clienti");
                        System.out.println("2. Afiseaza lista de conturi bancare");
                        System.out.println("3. Afiseaza tranzactiile unui client");
                        System.out.println("4. Adauga client");
                        System.out.println("5. Adauga cont bancar");
                        System.out.println("6. Atribuie un card de debit/credit unui cont bancar");
                        System.out.print("Alege o optiune: ");
                        int opt = scanner.nextInt();
                        scanner.nextLine();

                        switch (opt) {
                            case 0 -> meniuBanca = false;
                            case 1 -> serviciu.afiseazaClienti();
                            case 2 -> serviciu.afiseazaConturi();
                            case 3 -> serviciu.afiseazaTranzactiiClient(scanner);
                            case 4 -> serviciu.adaugaClient(scanner);
                            case 5 -> serviciu.adaugaCont(scanner);
                            case 6 -> serviciu.adaugaCard(scanner);
                            default -> System.out.println("Optiune invalida!");
                        }
                    }
                    break;

                case 2:
                    boolean meniuClient = true;
                    while (meniuClient) {
                        if (clientAutentificat == null) {
                            System.out.println("\n--- Meniu Client ---");
                            System.out.println("0. Inapoi");
                            System.out.println("1. Autentifica-te");
                            System.out.print("Alege o optiune: ");
                            int opt = scanner.nextInt();
                            scanner.nextLine();

                            switch (opt) {
                                case 0 -> meniuClient = false;
                                case 1 -> clientAutentificat = serviciu.autentificare(scanner);
                                default -> System.out.println("Optiune invalida!");
                            }
                        } else {
                            System.out.println("\n--- Meniu Client ---");
                            System.out.println("0. Inapoi");
                            System.out.println("1. Detalii client");
                            System.out.println("2. Plata noua");
                            System.out.println("3. Afiseaza tranzactiile");
                            System.out.println("4. Elimina card din cont");
                            System.out.println("5. Deconectare");
                            System.out.print("Alege o optiune: ");
                            int opt = scanner.nextInt();
                            scanner.nextLine();

                            switch (opt) {
                                case 0 -> meniuClient = false;
                                case 1 -> serviciu.detaliiClient(clientAutentificat);
                                case 2 -> serviciu.plataNoua(scanner, clientAutentificat);
                                case 3 -> serviciu.afiseazaTranzactiiClient(scanner, clientAutentificat);
                                case 4 -> serviciu.stergeCardClient(scanner, clientAutentificat);
                                case 5 -> {
                                    clientAutentificat = null;
                                    serviciu.setSesiuneCurenta();
                                    System.out.println("Deconectat cu succes.");
                                }
                                default -> System.out.println("Optiune invalida!");
                            }
                        }
                    }
                    break;

                default:
                    System.out.println("Optiune invalida!");
            }
        }

        scanner.close();
        System.out.println("Serviciul bancar s-a inchis.");
    }
}
