package net.bryanbergen.Skillinux.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConnection {

    private static final String USER_NAME = "skillinux";
    private static final String PASSWORD = "skillinux";
    private static final String HOST =  "jdbc:oracle:thin:@192.168.1.2:1521:xe";
    private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";

    private static DatabaseConnection instance;
    private Logger log = Logger.getLogger(getClass().getName());
    private Connection con;
    private PreparedStatement s;
    
    /**
     * Singleton 
     * @return 
     */
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
    
    private DatabaseConnection() {
    }
    
    private void connect() {
        try { 
            Class.forName(DRIVER);
            con = DriverManager.getConnection(HOST, USER_NAME, PASSWORD);
        } catch (SQLException e ) {
            log.log(Level.SEVERE, "Database Connection Failed");
        } catch (ClassNotFoundException e) {
            log.log(Level.SEVERE, "Driver Not Found");
        }
    }
    
    private void close() {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException sQLException) {
            log.log(Level.FINE, "Error closing");
        }
    }
    
    private ResultSet execute(String query) {
        ResultSet rs = null;
        connect();
        try {
            s = con.prepareStatement(query);
            rs = s.executeQuery();
            return rs;
        } catch (SQLException e) {
            log.log(Level.SEVERE, e.getMessage());
        }
        return rs;
    }
    
    
    /**
     * Just a simple test call to a test table to test
     * the connection
     * 
     * @return 
     */
    public String testCall() {
        String testQuery = "SELECT * FROM TEST";
        String output = "";
        ResultSet rs = execute(testQuery);
        try {
            while (rs.next()) {
                output += rs.getString(1) + "\n";
            }
        } catch (SQLException e) {
            log.log(Level.FINE, e.getMessage());
        } finally {
            close();
        }
        return output;
    }
}
