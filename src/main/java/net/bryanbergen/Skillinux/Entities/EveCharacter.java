package net.bryanbergen.Skillinux.Entities;

import org.apache.commons.lang3.builder.HashCodeBuilder;

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
    
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (!(o instanceof EveCharacter)) {
            return false;
        } else if (o == this) {
            return true;
        }
        EveCharacter c = (EveCharacter)o;
        
        if (!c.getName().equals(this.name)) {
            return false;
        } else if (c.getCharacterID() != this.characterID) {
            return false;
        } else {
            return c.getApi().equals(this.api);
        }
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17,31).
                append(name).
                append(characterID).
                append(api).toHashCode();
    }
    
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Name=").append(name);
        s.append(", CharacterID=").append(characterID);
        s.append(",\nAPI=").append(api);
        return s.toString();
    }
}
