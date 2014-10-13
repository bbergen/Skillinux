package net.bryanbergen.Skillinux.Entities;

public class EveCharacter {

    private String name;
    private API api;
    private long characterID;
    
    public EveCharacter() {
        this(null, null, -1);
    }
    
    public EveCharacter(String name, API api, long characterID) {
        this.name = name;
        this.api = api;
        this.characterID = characterID;
    }

    public String getName() {
        return name;
    }

    public API getApi() {
        return api;
    }

    public long getCharacterID() {
        return characterID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setApi(API api) {
        this.api = api;
    }

    public void setCharacterID(long characterID) {
        this.characterID = characterID;
    }
}
