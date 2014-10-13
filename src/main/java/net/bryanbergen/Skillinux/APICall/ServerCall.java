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
    
    ServerStatus("Server/ServerStatus", "Current Tranquility status and online player count.");
    
    private final String url;
    private final String description;
    
    private ServerCall(String url, String description) {
        this.url = url;
        this.description = description;
    }
    
    public String getUrl() {
        return url;
    }
    
    public String getDescription() {
        return description;
    }
}
