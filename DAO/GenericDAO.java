/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Database.Database;
import Entity.Entity;
import java.util.List;

/**
 *
 * @author flaviorgs
 */
public class GenericDAO<T extends Entity> implements DAO<T>  {
    
    
    private Class<T> clazz;

    public GenericDAO(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public void save(T entity) {
        Database.getInstance().save(clazz, entity);
    }

    @Override
    public T findById(int id) {
        return Database.getInstance().findById(clazz, id);
    }

    @Override
    public List<T> findAll() {
        return Database.getInstance().findAll(clazz);
    }

    @Override
    public void update(T entity) {
        Database.getInstance().update(clazz, entity);
    }

    @Override
    public void delete(int id) {
        Database.getInstance().delete(clazz, id);
    }

}
