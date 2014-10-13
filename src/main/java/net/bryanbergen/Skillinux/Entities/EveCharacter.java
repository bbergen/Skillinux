package net.bryanbergen.Skillinux.Entities;

public class EveCharacter {

    private String name;
    private API api;
    private long characterID;
    
    public EveCharacter() {
        
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
