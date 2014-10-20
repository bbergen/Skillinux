/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.bryanbergen.Skillinux.APICall;

/**
 *
 * @author bryan
 */
public enum AccountCall {
    
    AccountStatus("Account/AccountStatus", 33554432L, "Basic account information.", 60000L),
    APIKeyInfo("Account/APIKeyInfo", 0L, "Information about API and characters exposed by it.", 5000L),
    Characters("Account/Characters", 0L, "Returns a list of all characters on an account", 60000L);
    
    private final String url;
    private final String description;
    private final long accessMask;
    private final long cacheTime;
    
    private AccountCall(String url, long accessMask, String description, long cacheTime) {
        this.url = url;
        this.description = description;
        this.accessMask = accessMask;
        this.cacheTime = cacheTime;
    }
    
    public String getUrl() {
        return url;
    }
    
    public String getDescription() {
        return description;
    }
    
    public long getAccessMask() {
        return accessMask;
    }
    
    public long getCacheTime() {
        return cacheTime;
    }
}
