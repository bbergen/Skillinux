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
    
    ServerStatus("Server/ServerStatus");
    
    private String url;
    private ServerCall(String url) {
        this.url = url;
    }
    
    public String getUrl() {
        return url;
    }
}
