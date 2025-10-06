import org.mindrot.jbcrypt.BCrypt;

public class ServiciuParole {

    public static String cripteazaParola(String parolaClara) {
        return BCrypt.hashpw(parolaClara, BCrypt.gensalt());
    }

    public static boolean verificaParola(String parolaIntrodusa, String parolaHashed) {
        return BCrypt.checkpw(parolaIntrodusa, parolaHashed);
    }
}
