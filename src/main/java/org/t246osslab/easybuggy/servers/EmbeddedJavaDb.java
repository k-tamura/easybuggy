package org.t246osslab.easybuggy.servers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTransactionRollbackException;
import java.sql.Statement;
import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.t246osslab.easybuggy.utils.ApplicationUtils;
import org.t246osslab.easybuggy.utils.MessageUtils;

public class EmbeddedJavaDb {

    private static Logger log = LoggerFactory.getLogger(EmbeddedJavaDb.class);

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

    public String selectUsers(String name, String password, HttpServletRequest req) {

        String message = MessageUtils.getMsg("msg.error.user.not.exist", req.getLocale());
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();

            // query
            rs = stmt.executeQuery("SELECT * FROM users WHERE name='" + name + "' AND password='" + password + "'");

            StringBuilder sb = new StringBuilder();
            while (rs.next()) {
                sb.append(rs.getString("name") + ", " + rs.getString("secret") + "<BR>");
            }
            if (sb.length() > 0) {
                message = MessageUtils.getMsg("user.table.column.names", req.getLocale()) + "<BR>" + sb.toString();
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
        return message;
    }
    
    public String updateUsers2(int[] ids, Locale locale) {

        PreparedStatement stmt = null;
        Connection conn = null;
        int executeUpdate = 0;
        String message = "";
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
            message = MessageUtils.getMsg("msg.update.records", new Object[] { executeUpdate }, locale);

        } catch (SQLTransactionRollbackException e) {
            message = MessageUtils.getMsg("msg.deadlock.occurs", locale);
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
                message = MessageUtils.getMsg("msg.deadlock.occurs", locale);
            } else {
                message = MessageUtils.getMsg("msg.unknown.exception.occur", locale);
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
            message = MessageUtils.getMsg("easybuggy", locale);
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
        return message;
    }

}
