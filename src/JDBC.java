import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBC {
    private static final String URL = "jdbc:postgresql://localhost:5432/banca";
    private static final String USER = "denis";
    private static final String PASSWORD = "denis";

    private static Connection connection;

    private JDBC() {}

    public static Connection getInstance() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }
}
