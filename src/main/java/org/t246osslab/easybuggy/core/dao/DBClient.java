package org.t246osslab.easybuggy.core.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.t246osslab.easybuggy.core.utils.ApplicationUtils;

// TODO this class should be replaced another class
public class DBClient {

    private static Logger log = LoggerFactory.getLogger(DBClient.class);

    static {
        Statement stmt = null;
        Connection conn= null;
        try {
            String dbDriver = ApplicationUtils.getDatabaseDriver();
            if (dbDriver != null && !dbDriver.equals("")) {
                try {
                    Class.forName(dbDriver);
                } catch (Exception e) {
                    log.error("Exception occurs: ", e);
                }
            }
            String dbUrl = ApplicationUtils.getDatabaseURL();
            conn = DriverManager.getConnection(dbUrl);
            stmt = conn.createStatement();

            // create a sample user table
            createUsersTable(stmt);
            conn.setAutoCommit(false);

        } catch (SQLException e) {
            log.error("Exception occurs: ", e);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    log.error("Exception occurs: ", e);
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    log.error("Exception occurs: ", e);
                }
            }
        }
    }
    
    public Connection getConnection() throws SQLException {
        Connection conn = null;
        String dbDriver = ApplicationUtils.getDatabaseDriver();
        if (dbDriver != null && !dbDriver.equals("")) {
            try {
                Class.forName(dbDriver);
            } catch (Exception e) {
                log.error("Exception occurs: ", e);
            }
        }
        String dbUrl = ApplicationUtils.getDatabaseURL();
        conn = DriverManager.getConnection(dbUrl);
        return conn;
    }
    
    private static void createUsersTable(Statement stmt) throws SQLException {
        try {
            stmt.executeUpdate("drop table users");
        } catch (SQLException e) {
            // ignore exception if existing the table
        }
        // create users table
        stmt.executeUpdate("create table users (id varchar(10) primary key, name varchar(30), password varchar(30), secret varchar(100))");

        // insert sample records
        stmt.executeUpdate("insert into users values ('0','Mark','password','" + RandomStringUtils.randomNumeric(10) + "')");
        stmt.executeUpdate("insert into users values ('1','David','p@s2w0rd','" + RandomStringUtils.randomNumeric(10) + "')");
        stmt.executeUpdate("insert into users values ('2','Peter','pa33word','" + RandomStringUtils.randomNumeric(10) + "')");
        stmt.executeUpdate("insert into users values ('3','James','pathwood','" + RandomStringUtils.randomNumeric(10) + "')");
    }
}
