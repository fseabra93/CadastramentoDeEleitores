/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
        getTable(clazz).save(entity);
    }

    public <T extends Entity> T findById(Class<T> clazz, int id) {
        return getTable(clazz).findById(id);
    }

    public <T extends Entity> List<T> findAll(Class<T> clazz) {
        return getTable(clazz).findAll();
    }

    public <T extends Entity> void update(Class<T> clazz, T entity) {
        getTable(clazz).update(entity);
    }

    public <T extends Entity> void delete(Class<T> clazz, int id) {
        getTable(clazz).delete(id);
    }

    public static class DatabaseTable<T extends Entity> implements DatabaseTableI<T> {
        private Map<Integer, T> table = new HashMap<>();
        private int currentId = 1;

        @Override
        public void save(T entity) {
            entity.setId(currentId++);
            table.put(entity.getId(), entity);
        }

        @Override
        public T findById(int id) {
            return table.get(id);
        }

        @Override
        public List<T> findAll() {
            return new ArrayList<>(table.values());
        }

        @Override
        public void update(T entity) {
            table.put(entity.getId(), entity);
        }

        @Override
        public void delete(int id) {
            table.remove(id);
        }
    }
}
