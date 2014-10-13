package net.bryanbergen.Skillinux.APICall;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import net.bryanbergen.Skillinux.Entities.API;
import net.bryanbergen.Skillinux.Entities.EveCharacter;
import org.w3c.dom.Document;

public class ApiCall {

    public static final String EVE_BASE_URL = "https://api.eveonline.com/";
    public static final String EVE_URL_SUFFIX = ".xml.aspx";
    public static final int HTTP_ERROR = 400;
    
    private static ApiCall instance;
    private final DocumentBuilderFactory dbFactory;
    private final Logger log;

    /**
     * Singleton
     */
    private ApiCall() {
        log = Logger.getLogger(getClass().getName());
        dbFactory = DocumentBuilderFactory.newInstance();
    }
    
    public static ApiCall getInstance() {
        if (instance == null) {
            instance = new ApiCall();
        }
        return instance;
    }
    
    /**
     * Generates an XML Document with a server call response 
     * from https://api.eveonline.com/
     * 
     * @param api <code>API</code> passed as an argument to the call
     * @param call The type of server call. <br>
     * Currently supported Account calls:<br>
     *          <code>AccountCall.AccountStatus</code><br>
     *          <code>AccountCall.APIKeyInfo</code><br>
     *          <code>AccountCall.Characters</code>
     * @return XML Document containing the call response data.
     */
    public Document getAccountDocument(API api, AccountCall call) {
        Document xmlDoc = null;
        try {
            InputStream input;
            URLConnection con = new URL(buildURL(api, call.getUrl())).openConnection();
            HttpURLConnection hCon = (HttpURLConnection)con;
            if (hCon.getResponseCode() >= HTTP_ERROR) {
                input = hCon.getErrorStream();
            } else {
                input = hCon.getInputStream();
            }
            xmlDoc = buildXMLDoc(input);
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage());
        }
        return xmlDoc;
    }
    
    /**
     * Generates an XML Document with a server call response 
     * from https://api.eveonline.com/
     * 
     * @param character <code>EveCharacter</code> to pass as an argument to the call
     * @param call The type of server call.<br>
     * Currently support Character calls:<br>
     *          <code>CharacterCall.AccountBalance</code><br>
     *          <code>CharacterCall.CharacterSheet</code><br>
     *          <code>CharacterCall.SkillInTraining</code><br>
     *          <code>CharacterCall.SkillQueue</code><br>
     *          <code>CharacterCall.WalletJournal</code><br>
     *          <code>CharacterCall.WalletTransactions</code><br>
     * @return XML Document containing the call response data.
     */
    public Document getCharacterDocument(EveCharacter character, CharacterCall call) {
        Document xmlDoc = null;
        try {
            InputStream input;
            URLConnection con = new URL(buildURL(character.getApi(), call.getUrl(), character)).openConnection();
            HttpURLConnection hCon = (HttpURLConnection)con;
            if (hCon.getResponseCode() >= HTTP_ERROR) {
                input = hCon.getErrorStream();
            } else {
                input = hCon.getInputStream();
            }
            xmlDoc = buildXMLDoc(input);
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage());
        }
        return xmlDoc;
    }
    
    /**
     * Generates an XML Document with a server call response 
     * from https://api.eveonline.com/
     * 
     * @param call Type of server call.<br> 
     * Current only supports <code>ServerCall.ServerStatus</code>
     * @return XML Document containing the call response data.
     */
    public Document getServerDocument(ServerCall call) {
        Document xmlDoc = null;
        try {
            InputStream input;
            StringBuilder url = new StringBuilder();
            url.append(EVE_BASE_URL);
            url.append(call.getUrl());
            url.append(EVE_URL_SUFFIX);
            URLConnection con = new URL(url.toString()).openConnection();
            HttpURLConnection hCon = (HttpURLConnection)con;
            if (hCon.getResponseCode() >= HTTP_ERROR) {
                input = hCon.getErrorStream();
            } else {
                input = hCon.getInputStream();
            }
            xmlDoc = buildXMLDoc(input);
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage());
        }
        return xmlDoc;
    }
    
    private Document buildXMLDoc(InputStream input) throws Exception {
        DocumentBuilder builder = dbFactory.newDocumentBuilder();
        return builder.parse(input);
    }
    
    private String buildURL(API api, String baseURL) {
        return buildURL(api, baseURL, null);
    }
    
    private String buildURL(API api, String baseURL, EveCharacter character) {
        StringBuilder url = new StringBuilder();
        url.append(EVE_BASE_URL);
        url.append(baseURL);
        url.append(EVE_URL_SUFFIX);
        url.append("?keyID=");
        url.append(api.getKeyID());
        url.append("&vCode=");
        url.append(api.getvCode());
        url.append(character != null ? "&characterID=" : "");
        url.append(character != null ? character.getCharacterID() : "");
        return url.toString();
    }
}
