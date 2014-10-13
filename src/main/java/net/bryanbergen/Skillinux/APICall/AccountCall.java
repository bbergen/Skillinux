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
    
    AccountStatus("Account/AccountStatus"),
    APIKeyInfo("Account/APIKeyInfo"),
    Characters("Account/Characters");
    
    private final String url;
    private AccountCall(String url) {
        this.url = url;
    }
    
    public String getUrl() {
        return url;
    }
}
