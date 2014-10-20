package net.bryanbergen.Skillinux.Entities;

public class Skill {

    private int typeID;
    private String name;
    private String description;

    public Skill() {
    }
    
    public int getTypeID() {
        return typeID;
    }

    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

    public void setTypeID(int typeID) {
        this.typeID = typeID;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        StringBuilder api = new StringBuilder();
        api.append("TypeID=").append(typeID);
        api.append(",Name=").append(name);
        api.append(",Description=").append(description);
        return api.toString();
    }
}
