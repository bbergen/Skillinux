package net.bryanbergen.Skillinux.Queries;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import net.bryanbergen.Skillinux.Entities.API;
import net.bryanbergen.Skillinux.Entities.EveCharacter;

public class Query {

    public static final String BASE_URL = "https://api.eveonline.com/";
    public static final String BASE_SUFFIX = ".xml.aspx";
    private static final int HTTP_ERROR = 400;
    
    public InputStream getCharacterQuery(API api, EveCharacter character, QuerySubType type) {
        if (type.getQueryType() != QueryType.Character) {
            throw new IllegalArgumentException();
        }
        return null;
    }
    
    public InputStream getAccountQuery(API api, QuerySubType type) {
        if (type.getQueryType() != QueryType.Account) {
            throw new IllegalArgumentException();
        }
        InputStream input = null;
        try {
            URLConnection con = new URL(buildURL(api, type).toString()).openConnection();
            HttpURLConnection hCon = (HttpURLConnection) con;
            if (hCon.getResponseCode() >= HTTP_ERROR) {
                input = hCon.getErrorStream();
            } else {
                input = hCon.getInputStream();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } 
        return input;
    }
    
    public InputStream getServerQuery() {
        return null;
    }
    
    private StringBuilder buildURL(API api, QuerySubType query) {
        StringBuilder uri = new StringBuilder();
        uri.append(query.getURL());
        uri.append("?keyID=");
        uri.append(api.getKeyID());
        uri.append("&vCode=");
        uri.append(api.getvCode());
        return uri;
    }
}
