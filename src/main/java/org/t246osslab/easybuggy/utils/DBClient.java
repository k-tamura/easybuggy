package org.t246osslab.easybuggy.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTransactionRollbackException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBClient {

    private static Logger log = LoggerFactory.getLogger(DBClient.class);

    static Connection conn;

    static {
        Statement stmt = null;
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
            try {
                stmt.executeUpdate("drop table users");
            } catch (SQLException e) {
                // ignore exception if exist the table
            }
            // create users table
            stmt.executeUpdate("create table users (id int primary key, name varchar(30), password varchar(30), secret varchar(30))");

            // insert rows
            stmt.executeUpdate("insert into users values (0,'Mark','password','57249037993')");
            stmt.executeUpdate("insert into users values (1,'David','p@s2w0rd','42368923031')");
            stmt.executeUpdate("insert into users values (2,'Peter','pa33word','54238496555')");
            stmt.executeUpdate("insert into users values (3,'James','pathwood','70414823225')");
            
            try {
                stmt.executeUpdate("drop table users2");
            } catch (SQLException e) {
                // ignore exception if exist the table
            }
            // create users table
            stmt.executeUpdate("create table users2 (id int primary key, name varchar(30), password varchar(100))");

            // insert rows
            for (int i = 1; i <= 2; i++) {
                stmt.executeUpdate("insert into users2 values (" + i + ",'user" + i + "','password')");
            }

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
        }
    }

    public ArrayList<String[]> selectUsers(String name, String password) {

        Statement stmt = null;
        ResultSet rs = null;
        ArrayList<String[]> users = new ArrayList<String[]>();
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM users WHERE name='" + name + "' AND password='" + password + "'");
            while (rs.next()) {
                users.add(new String[]{rs.getString("name"), rs.getString("secret")});
            }
        } catch (Exception e) {
            log.error("Exception occurs: ", e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    log.error("Exception occurs: ", e);
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    log.error("Exception occurs: ", e);
                }
            }
        }
        return users;
    }
    
    public String updateUsers2(int[] ids, Locale locale) {

        PreparedStatement stmt = null;
        Connection conn = null;
        int executeUpdate = 0;
        String resultMessage = "";
        try {
            String dbUrl = ApplicationUtils.getDatabaseURL();
            String dbDriver = ApplicationUtils.getDatabaseDriver();
            if (dbDriver != null && !dbDriver.equals("")) {
                try {
                    Class.forName(dbDriver);
                } catch (Exception e) {
                    log.error("Exception occurs: ", e);
                }
            }
            conn = DriverManager.getConnection(dbUrl);
            conn.setAutoCommit(false);
            // conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            stmt = conn.prepareStatement("Update users2 set password = ?  where id = ?");
            stmt.setString(1, UUID.randomUUID().toString());
            stmt.setInt(2, ids[0]);
            executeUpdate = stmt.executeUpdate();

            Thread.sleep(5000);

            stmt.setString(1, UUID.randomUUID().toString());
            stmt.setInt(2, ids[1]);
            executeUpdate = executeUpdate + stmt.executeUpdate();
            conn.commit();
            resultMessage = MessageUtils.getMsg("msg.update.records", new Object[] { executeUpdate }, locale);

        } catch (SQLTransactionRollbackException e) {
            resultMessage = MessageUtils.getMsg("msg.deadlock.occurs", locale);
            log.error("Exception occurs: ", e);
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException e1) {
                    log.error("Exception occurs: ", e1);
                }
            }
        } catch (SQLException e) {
            if ("41000".equals(e.getSQLState())) {
                resultMessage = MessageUtils.getMsg("msg.deadlock.occurs", locale);
            } else {
                resultMessage = MessageUtils.getMsg("msg.unknown.exception.occur", locale);
            }
            log.error("Exception occurs: ", e);
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException e1) {
                    log.error("Exception occurs: ", e1);
                }
            }
        } catch (Exception e) {
            resultMessage = MessageUtils.getMsg("easybuggy", locale);
            log.error("Exception occurs: ", e);
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException e1) {
                    log.error("Exception occurs: ", e1);
                }
            }
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
        return resultMessage;
    }

}
