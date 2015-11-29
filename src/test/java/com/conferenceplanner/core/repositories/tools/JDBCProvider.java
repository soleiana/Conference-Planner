package com.conferenceplanner.core.repositories.tools;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCProvider {

    private static final String DB_CONFIG_FILE = "datasource.properties";
    private String dbUrl;
    private String userName;
    private String password;

    public JDBCProvider() {
        registerJDBCDriver();
        initDatabaseConnectionProperties();
    }

    private void registerJDBCDriver() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Exception while registering JDBC driver!");
            e.printStackTrace();
        }
    }

    private void initDatabaseConnectionProperties() {
        Properties properties = new Properties();
        try {
            properties.load(JDBCProvider.class.getClassLoader().getResourceAsStream(DB_CONFIG_FILE));
            dbUrl = properties.getProperty("dbUrl");
            userName = properties.getProperty("userName");
            password = properties.getProperty("password");
        } catch (IOException e){
            System.out.println("Exception while reading JDBC configuration from file = " + DB_CONFIG_FILE);
            e.printStackTrace();
        }
    }

    protected Connection getConnection() throws DBException {
        try{
            return DriverManager.getConnection(dbUrl, userName, password);
        } catch (SQLException e) {
            System.out.println("Exciption while getting connection to database");
            e.printStackTrace();
            throw new DBException(e);
        }
    }

    protected void closeConnection(Connection connection) throws DBException {
        try {
            if(connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("Exciption while closing connection to database");
            e.printStackTrace();
            throw new DBException(e);
        }
    }
}
