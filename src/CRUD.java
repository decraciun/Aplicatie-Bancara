import java.sql.SQLException;
import java.util.List;

public interface CRUD<T> {
    void create(T entity);
    T read(String id) throws SQLException;
    List<T> readAll();
    void update(T entity);
    void delete(String id);
}
