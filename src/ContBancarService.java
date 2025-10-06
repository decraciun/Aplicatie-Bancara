import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContBancarService extends Service<ContBancar> {

    private static ContBancarService instance;

    private ContBancarService() throws SQLException {
        super(JDBC.getInstance());
    }

    public static synchronized ContBancarService getInstance() throws SQLException {
        if (instance == null) {
            instance = new ContBancarService();
        }
        return instance;
    }

    @Override
    public void create(ContBancar cont) {
        String sql = "INSERT INTO cont_bancar (iban, client_cnp, sold) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, cont.getIBAN());
            stmt.setString(2, cont.getClient().getCnp());
            stmt.setDouble(3, cont.getSold());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ContBancar read(String iban) {
        String sql = "SELECT cb.*, c.nume, c.adresa, c.email, c.telefon, c.parola " +
                "FROM cont_bancar cb " +
                "JOIN client c ON cb.client_cnp = c.CNP " +
                "WHERE cb.iban = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, iban);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToContBancar(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<ContBancar> readAll() {
        List<ContBancar> conturi = new ArrayList<>();
        String sql = "SELECT cb.*, c.nume, c.adresa, c.email, c.telefon, c.parola " +
                "FROM cont_bancar cb " +
                "JOIN client c ON cb.client_cnp = c.CNP";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                conturi.add(mapResultSetToContBancar(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conturi;
    }

    @Override
    public void update(ContBancar cont) {
        String sql = "UPDATE cont_bancar SET sold = ? WHERE iban = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, cont.getSold());
            stmt.setString(2, cont.getIBAN());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String iban) {
        String sql = "DELETE FROM cont_bancar WHERE iban = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, iban);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ContBancar mapResultSetToContBancar(ResultSet rs) throws SQLException {
        String iban = rs.getString("iban");
        String cnp = rs.getString("client_cnp");
        String nume = rs.getString("nume");
        String adresaStr = rs.getString("adresa");
        Adresa adresa = parseAdresa(adresaStr);
        String email = rs.getString("email");
        String telefon = rs.getString("telefon");
        String parola = rs.getString("parola");
        double sold = rs.getDouble("sold");

        Client client = new Client(nume, cnp, adresa, email, telefon, parola);
        ContBancar cont = new ContBancar(iban, client);
        cont.setSold(sold);
        return cont;
    }

    private Adresa parseAdresa(String adresaStr) {
        String[] parts = adresaStr.split(",\\s*");

        if (parts.length == 4) {
            return new Adresa(parts[0], parts[1], parts[2], parts[3]);
        } else if (parts.length == 7) {
            return new Adresa(parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6]);
        } else {
            throw new IllegalArgumentException("Format necunoscut pentru adresa: " + adresaStr);
        }
    }
}
