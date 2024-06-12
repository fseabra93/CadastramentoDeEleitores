/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Database;

import Entity.Entity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author flaviorgs
 */
    public class DatabaseTable<T extends Entity> implements DatabaseTableI<T> {
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
