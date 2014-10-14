package net.bryanbergen.Skillinux.Entities;

public class Skill {

    private int typeID;
    private String name;

    public Skill() {
    }
    
    public int getTypeID() {
        return typeID;
    }

    public String getName() {
        return name;
    }

    public void setTypeID(int typeID) {
        this.typeID = typeID;
    }

    public void setName(String name) {
        this.name = name;
    }
}
