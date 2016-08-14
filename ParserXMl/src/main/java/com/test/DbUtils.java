package com.test;

import org.apache.log4j.Logger;
import java.sql.*;



public class DbUtils {
    // JDBC URL, username and password of postgresql server
    private static final String url = "jdbc:postgresql://localhost:8848/test";
    private static final String user = "root";
    private static final String password = "root";

    // JDBC variables for opening and managing connection
    private static Connection con;
    private static Statement stmt;

    // Logger
    private static final Logger logger = Logger.getLogger(DbUtils.class);

    public static void putToDB(String content, String creationDate) {
        String query = "INSERT INTO xml_table (content,creationDate) VALUES("+content+","+creationDate+")";

            try {
                // opening database connection to DbUtils server
                DriverManager.setLoginTimeout(10);
                con = DriverManager.getConnection(url, user, password);
                // getting Statement object to execute query
                stmt = con.createStatement();
                // executing SELECT query
                stmt.executeQuery(query);
            } catch (SQLException sqlEx) {
                logger.error("Connecting failed");
            } finally {
                //close connection ,stmt and resultset here
                try {
                    con.close();
                } catch (SQLException se) { logger.error("Failed to close connection"); }
                try {
                    stmt.close();
                } catch (SQLException se) { logger.error("Failed to close connection"); }

            }

    }
}
