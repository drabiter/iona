package com.drabiter.iona.db;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;

public class Database {

    private static Database instance = new Database();

    private JdbcPooledConnectionSource connectionPool;

    private Map<Class<?>, Dao<? extends Object, ?>> daoPool = new HashMap<>();

    public static Database get() {
        return instance;
    }

    public void connection(DatabaseProperty dbProperty) throws SQLException {
        connectionPool = new JdbcPooledConnectionSource(dbProperty.getUrl(), dbProperty.getUser(), dbProperty.getPassword());
    }

    public <T, I> void addDao(Class<T> clazz, Class<I> idType) throws SQLException {
        Dao<T, I> dao = DaoManager.createDao(connectionPool, clazz);
        daoPool.put(clazz, dao);
    }

    @SuppressWarnings("unchecked")
    public <T> Dao<T, ?> getDao(Class<T> clazz) {
        return (Dao<T, ?>) daoPool.get(clazz);
    }

    @SuppressWarnings("unchecked")
    public <T, I> Dao<T, I> getDao(Class<T> clazz, Class<I> type) {
        return (Dao<T, I>) daoPool.get(clazz);
    }

    public JdbcPooledConnectionSource getConnectionPool() {
        return connectionPool;
    }

}
