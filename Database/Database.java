package Database;

import Entity.Entity;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class Database {
    private static Database instance;
    private Map<Class<? extends Entity>, DatabaseTableI<? extends Entity>> tables = new HashMap<>();

    private Database() {}

    public static synchronized Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public <T extends Entity> void addTable(Class<T> clazz, DatabaseTableI<T> table) {
        tables.put(clazz, table);
    }

    @SuppressWarnings("unchecked")
    public <T extends Entity> DatabaseTableI<T> getTable(Class<T> clazz) {
        return (DatabaseTableI<T>) tables.get(clazz);
    }

    public <T extends Entity> void save(Class<T> clazz, T entity) {
        DatabaseTableI<T> table = getTable(clazz);
        if (table == null) {
            table = new DatabaseTable<>();
            addTable(clazz, table);
        }
        table.save(entity);
    }

    public <T extends Entity> T findById(Class<T> clazz, int id) {
        DatabaseTableI<T> table = getTable(clazz);
        if (table == null) {
            return null;
        }
        return table.findById(id);
    }

    public <T extends Entity> List<T> findAll(Class<T> clazz) {
        DatabaseTableI<T> table = getTable(clazz);
        if (table == null) {
            return new ArrayList<>();
        }
        return table.findAll();
    }

    public <T extends Entity> void update(Class<T> clazz, T entity) {
        DatabaseTableI<T> table = getTable(clazz);
        if (table != null) {
            table.update(entity);
        }
    }

    public <T extends Entity> void delete(Class<T> clazz, int id) {
        DatabaseTableI<T> table = getTable(clazz);
        if (table != null) {
            table.delete(id);
        }
    }


}
