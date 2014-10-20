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
import net.bryanbergen.Skillinux.Entities.EveCharacter;
import net.bryanbergen.Skillinux.Entities.Skill;
import net.bryanbergen.Skillinux.Util.CalendarUtil;

public class DatabaseConnection {

    private static final String USER_NAME = "skillinux";
    private static final String PASSWORD = "skillinux";
//    private static final String HOST_ORACLE =  "jdbc:oracle:thin:@192.168.1.2:1521:xe";
    private static final String HOST_MYSQL = "jdbc:mysql://localhost/skx";
//    private static final String DRIVER_ORACLE = "oracle.jdbc.driver.OracleDriver";
    private static final String DRIVER_MYSQL = "com.mysql.jdbc.Driver";

    private static DatabaseConnection instance;
    private static final Logger log = Logger.getLogger(DatabaseConnection.class.getName());
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
    
    public void connect() {
        try { 
            Class.forName(DRIVER_MYSQL);
            con = DriverManager.getConnection(HOST_MYSQL, USER_NAME, PASSWORD);
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
    
    public Skill getSkill(int typeID) {
        Skill skill = new Skill();
        connect();
        
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT *\n");
        sql.append("FROM skills\n");
        sql.append("WHERE typeID = ?");
        
        try {
            s = con.prepareStatement(sql.toString());
            s.setInt(1, typeID);
            rs = s.executeQuery();
            
            rs.next();
            skill.setTypeID(rs.getInt(1));
            skill.setName(rs.getString(2));
            skill.setDescription(rs.getString(3));
        } catch (SQLException e) {
            log.log(Level.SEVERE, e.getMessage());
        } finally {
            close();
        }
        
        return skill;
    }
    
    /**
     * Saves an <code>API</code> key into the database.
     * 
     * @param api Key to be saved.
     */
    public void saveAPI(API api) {
        connect();

        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO apiKeys (keyid, vcode, accessmask, expiry)\n");
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
            rs = quickExecute("SELECT * FROM apiKeys");
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
    
    /**
     * Loads a single <code>API</code> from the Database
     * 
     * @param keyID ID of the <code>API</code> requested
     * @return the <code>API</code> matching the <code>keyID</code>.
     */
    public API loadAPI(long keyID) {
        API api = new API();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM apiKeys\n");
        sql.append("WHERE keyid = ?");
        
        try {
            connect();
            s = con.prepareStatement(sql.toString());
            s.setLong(1, keyID);
            rs = s.executeQuery();
            
            rs.next();
            api.setKeyID(rs.getLong(1));
            api.setvCode(rs.getString(2));
            api.setAccessMask(rs.getLong(3));
            api.setExpiry(CalendarUtil.getCalendarFromSQLDate(rs.getDate(4)));
            
        } catch (SQLException e) {
            log.log(Level.SEVERE, e.getMessage());
        } finally {
            close();
        }
        
        return api;
    }
    
    /**
     * Saves an <code>EveCharacter</code> to the database<br>
     * Tied <code>API</code> must already exist in the database
     * prior to saving the character. 
     * 
     * @param character The character to save.
     */
    public void saveCharacter(EveCharacter character) {
        connect();
        
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO characters (characterid, name)");
        sql.append("VALUES (");
        sql.append("?, ?").append(")");
        
        try {
            s = con.prepareStatement(sql.toString());
            s.setLong(1, character.getCharacterID());
            s.setString(2, character.getName());
            s.execute();
        } catch (SQLException e) {
            log.log(Level.SEVERE, e.getMessage());
        } finally {
            close();
        }
        updateAPIExposure(character.getCharacterID(), character.getApi().getKeyID());
    }
    
    private void updateAPIExposure(long characterID, long keyID) {
        connect();
        
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO keyExposures (characterid, apiid)");
        sql.append("VALUES (");
        sql.append("?, ?").append(")");
        
        try {
            s = con.prepareStatement(sql.toString());
            s.setLong(1, characterID);
            s.setLong(2, keyID);
            s.execute();
        } catch (SQLException e) {
            log.log(Level.SEVERE, e.getMessage());
        } finally {
            close();
        }
    }
    
    /**
     * Builds a list of all stored Characters.
     * 
     * @return All persistant <code>EveCharacter</code> objects.
     */
    public List<EveCharacter> loadAllCharacters() {
        connect();
        List<EveCharacter> characters = new ArrayList<EveCharacter>();

        try {
            rs = quickExecute("SELECT * FROM characters");
            while (rs.next()) {
                EveCharacter c = new EveCharacter();
                c.setCharacterID(rs.getLong(1));
                c.setName(rs.getString(2));
                characters.add(c);
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, e.getMessage());
        } finally {
            close();
        }
        
        for (EveCharacter c : characters) {
            c.setApi(getHighestMaskedAPI(c.getCharacterID()));
        }
        
        return characters;
    }
    
    private API getHighestMaskedAPI(long characterID) {
        connect();
        long keyID = -1;
        
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT k.keyid, MAX(accessMask)\n");
        sql.append("FROM keyExposures e, apiKeys k\n");
        sql.append("WHERE e.apiid = k.keyid\n");
        sql.append("AND e.characterid = ?\n");
        sql.append("GROUP BY k.keyID");
        
        try {
            s = con.prepareStatement(sql.toString());
            s.setLong(1, characterID);
            rs = s.executeQuery();
            rs.next();
            keyID = rs.getLong(1);
        } catch (SQLException e) {
            log.log(Level.SEVERE, e.getMessage());
        } finally {
            close();
        }
        
        return loadAPI(keyID);
    }
    
    /**
     * Fetches a list of all saved Characters exposed by an API Key
     * 
     * @param api the key to check for exposed characters
     * @return List of <code>EveCharacter</code> objects representing exposed characters
     */
    public List<EveCharacter> loadCharactersExposedBy(API api) {
        connect();
        List<EveCharacter> characters = new ArrayList<EveCharacter>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT c.name, characterid\n");
        sql.append("FROM keyExposures e INNER JOIN characters c\n");
        sql.append("USING(characterid)\n");
        sql.append("WHERE e.apiid = ?\n");
        
        try {
            s = con.prepareStatement(sql.toString());
            s.setLong(1, api.getKeyID());
            rs = s.executeQuery();
            while(rs.next()) {
                EveCharacter c = new EveCharacter();
                c.setName(rs.getString(1));
                c.setCharacterID(rs.getLong(2));
                c.setApi(api);
                characters.add(c);
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, e.getMessage());
        } finally {
            close();
        }
        return characters;
    }
    
    /**
     * Fetches a list of all API keys that expose a particular character.
     * 
     * @param characterID ID of the <code>EveCharacter</code> exposed
     * @return All keys exposing the <code>EveCharacter</code>
     */
    public List<API> loadAPIsExposing(long characterID) {
        connect();
        List<API> keys = new ArrayList<API>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT k.keyid, k.vcode, k.accessmask, k.expiry\n");
        sql.append("FROM keyExposures e, apiKeys k\n");
        sql.append("WHERE e.apiid = k.keyid\n");
        sql.append("AND e.characterid = ?\n");
        
        try {
            s = con.prepareStatement(sql.toString());
            s.setLong(1, characterID);
            rs = s.executeQuery();
            
            while (rs.next()) {
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
