package net.bryanbergen.Skillinux.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.bryanbergen.Skillinux.Entities.API;
import net.bryanbergen.Skillinux.Util.CalendarUtil;

public class DatabaseConnection {

    private static final String USER_NAME = "skillinux";
    private static final String PASSWORD = "skillinux";
    private static final String HOST =  "jdbc:oracle:thin:@192.168.1.2:1521:xe";
    private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";

    private static DatabaseConnection instance;
    private Logger log = Logger.getLogger(getClass().getName());
    private Connection con;
    private PreparedStatement s;
    private ResultSet rs;
    
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
    
    private ResultSet quickExecute(String query) throws SQLException {
        connect();
        s = con.prepareStatement(query);
        rs = s.executeQuery();
        return rs;
    }
    
    /**
     * Saves an <code>API</code> key into the database.
     * 
     * @param api Key to be saved.
     */
    public void saveAPI(API api) {
        connect();

        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO keys (keyid, vcode, accessmask, expiry)\n");
        sql.append("VALUES (");
        sql.append("?, ?, ?, ?");
        sql.append(")");
        
        try {
            s = con.prepareStatement(sql.toString());
            s.setLong(1, api.getKeyID());
            s.setString(2, api.getvCode());
            s.setLong(3, api.getAccessMask());
            s.setDate(4, CalendarUtil.getSQLDate(api.getExpiry()));
            s.execute();
            con.commit();
        } catch (SQLException e) {
            log.log(Level.SEVERE, e.getMessage());
        } finally {
            close();
        }
    }
    
    /**
     * Loads all <code>API</code> stored in the database.
     * 
     * @return All saved API Keys
     */
    public List<API> loadAPIs() {
        List<API> keys = new ArrayList<API>();
        
        try {
            rs = quickExecute("SELECT * FROM keys");
            while(rs.next()) {
                API key = new API();
                key.setKeyID(rs.getLong(1));
                key.setvCode(rs.getString(2));
                key.setAccessMask(rs.getLong(3));
                key.setExpiry(CalendarUtil.getCalendarFromSQLDate(rs.getDate(4)));
                keys.add(key);
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, e.getMessage());
        } finally {
            close();
        }
        return keys;
    }
}
