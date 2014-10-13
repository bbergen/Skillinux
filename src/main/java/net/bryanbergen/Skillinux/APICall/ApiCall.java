package net.bryanbergen.Skillinux.APICall;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.bryanbergen.Skillinux.Entities.API;

public class ApiCall {

    public static final String EVE_BASE_URL = "https://api.eveonline.com/";
    public static final String EVE_URL_SUFFIX = ".xml.aspx";
    public static final int HTTP_ERROR = 400;
    
    private static ApiCall instance;
    private Logger log = Logger.getLogger(getClass().getName());

    /**
     * Singleton
     */
    private ApiCall() {
    }
    
    public static ApiCall getInstance() {
        if (instance == null) {
            instance = new ApiCall();
        }
        return instance;
    }
    
    public InputStream getAccountDocument(API api, AccountCall call) {
        InputStream input = null;
        try {
            URLConnection con = new URL(buildURL(api, call.getUrl())).openConnection();
            HttpURLConnection hCon = (HttpURLConnection)con;
            if (hCon.getResponseCode() >= HTTP_ERROR) {
                input = hCon.getErrorStream();
            } else {
                input = hCon.getInputStream();
            }
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage());
        }
        return input;
    }
    
    public InputStream getCharacterDocument(API api, CharacterCall call) {
        InputStream input = null;
        //TODO build connection
        return input;
    }
    
    public InputStream getServerDocument(API api, ServerCall call) {
        InputStream input = null;
        //TODO build connection
        return input;
    }
    
    private String buildURL(API api, String baseURL) {
        StringBuilder url = new StringBuilder();
        url.append(EVE_BASE_URL);
        url.append(baseURL);
        url.append(EVE_URL_SUFFIX);
        url.append("?keyID=");
        url.append(api.getKeyID());
        url.append("&vCode=");
        url.append(api.getvCode());
        return url.toString();
    }
}
