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
public enum CharacterCall {
    
    AccountBalance("Char/AccountBalance"),
    CharacterSheet("Char/CharacterSheet"),
    SkillInTraining("Char/SkillInTraining"),
    SkillQueue("Char/Skillqueue"),
    WalletJournal("Char/WalletJournal"),
    WalletTransactions("Char/WalletTransactions");
    
    private String url;
    private CharacterCall(String url ) {
        this.url = url;
    }
    
    public String getUrl() {
        return url;
    }
}
