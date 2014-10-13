/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.bryanbergen.Skillinux.Queries;

/**
 *
 * @author bryan
 */
public enum QuerySubType {
    
    AccountStatus("Account/AccountStatus", QueryType.Account),
    APIKeyInfo("Account/APIKeyInfo", QueryType.Account),
    Characters("Account/Characters", QueryType.Account),
    AccountBalance("Char/AccountBalance", QueryType.Character),
    CharacterSheet("Char/CharacterSheet", QueryType.Character),
    SkillInTraining("Char/SkillInTraining", QueryType.Character ),
    SkillQueue("Char/SKilQueue", QueryType.Character),
    WalletJournal("Char/WalletJournal", QueryType.Character),
    WalletTransactions("Char/WalletTransactions", QueryType.Character),
    ServerStatus("Server/ServerStatus", QueryType.Character);
    
    private final String url;
    private final QueryType queryType;
    
    private QuerySubType(String url, QueryType queryType) {
        this.url = Query.BASE_URL + url + Query.BASE_SUFFIX;
        this.queryType = queryType;
    }
    
    public String getURL() {
        return url;
    }
    
    public QueryType getQueryType() {
        return queryType;
    }
}
