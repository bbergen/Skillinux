package net.bryanbergen.Skillinux.Entities;

import java.util.Calendar;

public class Skill {

    private int typeID;
    private String name;
    private String description;
    private Calendar trainingStartTime;
    private Calendar trainingEndTime;
    private int trainingStartSP;
    private int trainingDestinationSP;
    private int trainingToLevel;

    public Skill() {
    }
    
    public Skill(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Calendar getTrainingStartTime() {
        return trainingStartTime;
    }

    public void setTrainingStartTime(Calendar trainingStartTime) {
        this.trainingStartTime = trainingStartTime;
    }

    public Calendar getTrainingEndTime() {
        return trainingEndTime;
    }

    public void setTrainingEndTime(Calendar trainingEndTime) {
        this.trainingEndTime = trainingEndTime;
    }

    public int getTrainingStartSP() {
        return trainingStartSP;
    }

    public void setTrainingStartSP(int trainingStartSP) {
        this.trainingStartSP = trainingStartSP;
    }

    public int getTrainingDestinationSP() {
        return trainingDestinationSP;
    }

    public void setTrainingDestinationSP(int trainingDestinationSP) {
        this.trainingDestinationSP = trainingDestinationSP;
    }

    public int getTrainingToLevel() {
        return trainingToLevel;
    }

    public void setTrainingToLevel(int trainingToLevel) {
        this.trainingToLevel = trainingToLevel;
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
