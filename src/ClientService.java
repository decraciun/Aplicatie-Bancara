import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientService extends Service<Client> {

    private static ClientService instance;

    private ClientService() throws SQLException {
        super(JDBC.getInstance());
    }

    public static synchronized ClientService getInstance() throws SQLException {
        if (instance == null) {
            instance = new ClientService();
        }
        return instance;
    }

    @Override
    public void create(Client client) {
        String sql = "INSERT INTO client (CNP, nume, adresa, email, telefon, parola) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, client.getCnp());
            stmt.setString(2, client.getNume());
            stmt.setString(3, client.getAdresa().toString());
            stmt.setString(4, client.getEmail());
            stmt.setString(5, client.getTelefon());
            stmt.setString(6, client.getParola());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Client read(String email) {
        String sql = "SELECT * FROM client WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, String.valueOf(email));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToClient(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Client findByCnp(String cnp) {
        for (Client c : readAll()) {
            if (c.getCnp().equals(cnp)) {
                return c;
            }
        }
        return null;
    }

    @Override
    public List<Client> readAll() {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM client";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                clients.add(mapResultSetToClient(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clients;
    }

    @Override
    public void update(Client client) {
        String sql = "UPDATE client SET adresa = ?, email = ?, telefon = ?, parola = ? WHERE CNP = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, client.getAdresa().toString());
            stmt.setString(2, client.getEmail());
            stmt.setString(3, client.getTelefon());
            stmt.setString(4, client.getParola());
            stmt.setString(5, client.getCnp());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM client WHERE CNP = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, String.valueOf(id));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    private Client mapResultSetToClient(ResultSet rs) throws SQLException {
        String CNP = rs.getString("CNP");
        String nume = rs.getString("nume");
        String adresaStr = rs.getString("adresa");
        Adresa adresa = parseAdresa(adresaStr);
        String email = rs.getString("email");
        String telefon = rs.getString("telefon");
        String parola = rs.getString("parola");

        return new Client(nume, CNP, adresa, email, telefon, parola);
    }
}
