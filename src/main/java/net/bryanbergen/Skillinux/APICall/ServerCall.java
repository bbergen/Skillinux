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
public enum ServerCall {
    
    ServerStatus("Server/ServerStatus", "Current Tranquility status and online player count.", 3000L);
    
    private final String url;
    private final String description;
    private final long cacheTime;
    
    private ServerCall(String url, String description, long cacheTime) {
        this.url = url;
        this.description = description;
        this.cacheTime = cacheTime;
    }
    
    public String getUrl() {
        return url;
    }
    
    public String getDescription() {
        return description;
    }
    
    public long getCacheTime() {
        return cacheTime;
    }
}
