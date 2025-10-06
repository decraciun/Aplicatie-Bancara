import java.sql.Connection;

public abstract class Service<T> implements CRUD<T> {
    protected Connection connection;

    public Service(Connection connection) {
        this.connection = connection;
    }
}
