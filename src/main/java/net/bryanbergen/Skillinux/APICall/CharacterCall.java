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
    
    AccountBalance("Char/AccountBalance", 1L, "Isk balance of character.", 15000L),
    CharacterSheet("Char/CharacterSheet", 8L, "Attributes related to character.", 60000L),
    SkillInTraining("Char/SkillInTraining", 131072L, "Skill the character is currently training.", 60000L),
    SkillQueue("Char/Skillqueue", 262144L, "Skill queue of the character.", 60000L),
    WalletJournal("Char/WalletJournal", 2097152L, "List of journal transactions for character.", 30000L),
    WalletTransactions("Char/WalletTransactions", 4194304L, "List of market transactions for character", 30000L);
    
    private final String url;
    private final String description;
    private final long accessMask;
    private final long cacheTime;
    
    private CharacterCall(String url, long accessMask, String description, long cacheTime ) {
        this.url = url;
        this.accessMask = accessMask;
        this.description = description;
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
