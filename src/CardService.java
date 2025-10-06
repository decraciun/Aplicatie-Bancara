import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CardService extends Service<Card> {
    private static CardService instance;

    private CardService() throws SQLException {
        super(JDBC.getInstance());
    }

    public static synchronized CardService getInstance() throws SQLException {
        if (instance == null) {
            instance = new CardService();
        }
        return instance;
    }

    @Override
    public void create(Card card) {
        String sql = "INSERT INTO card (numar_card, data_expirare, cvv, cont_id, tip, limita_credit) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, card.numarCard);
            stmt.setString(2, card.dataExpirare);
            stmt.setString(3, card.CVV);
            stmt.setString(4, card.getCont().getIBAN());

            if (card instanceof CardCredit) {
                stmt.setString(5, "credit");
                stmt.setDouble(6, ((CardCredit) card).getLimitaCredit());
            } else {
                stmt.setString(5, "debit");
                stmt.setNull(6, Types.DOUBLE);
            }

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Card read(String numarCard) {
        String sql = "SELECT * FROM card WHERE numar_card = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, numarCard);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToCard(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Card> readAll() {
        List<Card> cards = new ArrayList<>();
        String sql = "SELECT * FROM card";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                cards.add(mapResultSetToCard(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cards;
    }

    @Override
    public void update(Card card) {
        String sql = "UPDATE card SET data_expirare = ?, cvv = ?, cont_id = ?, tip = ?, limita_credit = ? WHERE numar_card = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, card.dataExpirare);
            stmt.setString(2, card.CVV);
            stmt.setString(3, card.getCont().getIBAN());

            if (card instanceof CardCredit) {
                stmt.setString(4, "credit");
                stmt.setDouble(5, ((CardCredit) card).getLimitaCredit());
            } else {
                stmt.setString(4, "debit");
                stmt.setNull(5, Types.DOUBLE);
            }

            stmt.setString(6, card.numarCard);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String numarCard) {
        String sql = "DELETE FROM card WHERE numar_card = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, numarCard);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Card mapResultSetToCard(ResultSet rs) throws SQLException {
        String numarCard = rs.getString("numar_card");
        String dataExpirare = rs.getString("data_expirare");
        String cvv = rs.getString("cvv");
        String contId = rs.getString("cont_id");
        String tip = rs.getString("tip");
        double limitaCredit = rs.getDouble("limita_credit");

        ContBancar cont = ContBancarService.getInstance().read(contId);

        if ("credit".equalsIgnoreCase(tip)) {
            return new CardCredit(numarCard, dataExpirare, cvv, cont, limitaCredit);
        } else {
            return new CardDebit(numarCard, dataExpirare, cvv, cont);
        }
    }
}
