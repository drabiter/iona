package com.drabiter.iona.db;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class Database {

    private JdbcPooledConnectionSource connectionPool;

    private Map<Class<?>, Dao<? extends Object, ?>> daoPool = new HashMap<>();

    public Database(DatabaseProperty dbProperty) throws SQLException {
        connectionPool = new JdbcPooledConnectionSource(dbProperty.getUrl(), dbProperty.getUser(), dbProperty.getPassword());
    }

    public <T> void createTable(Class<T> modelType) throws SQLException {
        TableUtils.createTableIfNotExists(connectionPool, modelType);
    }

    public <T, I> void addDao(Class<T> modelType, Class<I> idType) throws SQLException {
        Dao<T, I> dao = DaoManager.createDao(connectionPool, modelType);
        daoPool.put(modelType, dao);
    }

    public <T> int create(Class<T> modelType, T object) throws SQLException {
        return getDao(modelType).create(object);
    }

    public <T> int update(Class<T> modelType, T object) throws SQLException {
        return getDao(modelType).update(object);
    }

    // TODO read and delete

    @SuppressWarnings("unchecked")
    public <T> Dao<T, ?> getDao(Class<T> modelType) {
        return (Dao<T, ?>) daoPool.get(modelType);
    }

    public JdbcPooledConnectionSource getConnectionPool() {
        return connectionPool;
    }

}
