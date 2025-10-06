import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TranzactieService extends Service<Tranzactie> {
    private static TranzactieService instance;

    private TranzactieService() throws SQLException {
        super(JDBC.getInstance());
    }

    public static synchronized TranzactieService getInstance() throws SQLException {
        if (instance == null) {
            instance = new TranzactieService();
        }
        return instance;
    }

    @Override
    public void create(Tranzactie tranzactie) {
        String sql = "INSERT INTO tranzactie (cont_sursa_id, cont_destinatie_id, descriere, suma, data, tip) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, tranzactie.getSursa().getIBAN());

            if (tranzactie.getDestinatie() != null) {
                stmt.setString(2, tranzactie.getDestinatie().getIBAN());
            } else {
                stmt.setNull(2, Types.VARCHAR);
            }

            stmt.setString(3, tranzactie.getDescriere());
            stmt.setDouble(4, tranzactie.getSuma());
            stmt.setTimestamp(5, Timestamp.valueOf(tranzactie.getData()));
            stmt.setString(6, tranzactie.getTip());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    tranzactie.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Crearea tranzactiei a esuat, nu s-a generat ID.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Eroare la crearea tranzactiei", e);
        }
    }

    @Override
    public Tranzactie read(String id) {
        String sql = "SELECT * FROM tranzactie WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(id));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToTranzactie(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Tranzactie> readAll() {
        List<Tranzactie> tranzactii = new ArrayList<>();
        String sql = "SELECT * FROM tranzactie";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                tranzactii.add(mapResultSetToTranzactie(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tranzactii;
    }

    @Override
    public void update(Tranzactie tranzactie) {
        String sql = "UPDATE tranzactie SET cont_sursa_id = ?, cont_destinatie_id = ?, descriere = ?, suma = ?, data = ?, tip = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, tranzactie.getSursa().getIBAN());
            if (tranzactie.getDestinatie() != null) {
                stmt.setString(2, tranzactie.getDestinatie().getIBAN());
            } else {
                stmt.setNull(2, Types.VARCHAR);
            }
            stmt.setString(3, tranzactie.getDescriere());
            stmt.setDouble(4, tranzactie.getSuma());
            stmt.setTimestamp(5, Timestamp.valueOf(tranzactie.getData()));
            stmt.setString(6, tranzactie.getTip());
            stmt.setInt(7, tranzactie.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM tranzactie WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(id));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Tranzactie mapResultSetToTranzactie(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String sursaId = rs.getString("cont_sursa_id");
        String destinatieId = rs.getString("cont_destinatie_id");
        String descriere = rs.getString("descriere");
        double suma = rs.getDouble("suma");
        LocalDateTime data = rs.getTimestamp("data").toLocalDateTime();
        String tip = rs.getString("tip");

        ContBancar sursa = ContBancarService.getInstance().read(sursaId);
        ContBancar destinatie = destinatieId != null ? ContBancarService.getInstance().read(destinatieId) : null;

        return new Tranzactie(id, sursa, destinatie, descriere, suma, data, tip);
    }
}
