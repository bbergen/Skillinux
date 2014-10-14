package net.bryanbergen.Skillinux.XMLParser;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import net.bryanbergen.Skillinux.APICall.AccountCall;
import net.bryanbergen.Skillinux.APICall.ApiCall;
import net.bryanbergen.Skillinux.APICall.CharacterCall;
import net.bryanbergen.Skillinux.APICall.ServerCall;
import net.bryanbergen.Skillinux.Entities.API;
import net.bryanbergen.Skillinux.Entities.EveCharacter;
import net.bryanbergen.Skillinux.Entities.Skill;
import org.w3c.dom.Document;

public class Parser {

    private final ApiCall caller = ApiCall.getInstance();
    private API api;
    private EveCharacter character;
    
    public Parser() {
    }
    
    public Parser(API api) {
        this.api = api;
    }
    
    public Parser(EveCharacter character) {
        this.character = character;
    }
    
    public void setCharacter(EveCharacter character) {
        this.character = character;
    }
    
    public void setAPI(API api) {
        this.api = api;
    }
    
    public EveCharacter getCharacter() {
        return character;
    }
    
    public API getAPI() {
        return api;
    }
    
    public List<EveCharacter> getCharacters() {
        Document xmlDoc = caller.getAccountDocument(api, AccountCall.Characters);
        //TODO parsing logic on xmlDoc
        return Collections.EMPTY_LIST;
    }
    
    public Calendar getAccountExpiry() {
        Document xmlDoc = caller.getAccountDocument(api, AccountCall.AccountStatus);
        //TODO parse xmlDoc for account expiry date
        return null;
    }
    
    public boolean isAccountActive() {
        Document xmlDoc = caller.getAccountDocument(api, AccountCall.AccountStatus);
        //TODO parse xmlDoc for account active status
        return false;
    }
    
    public long getWalletBalance() {
        Document xmlDoc = caller.getCharacterDocument(character, CharacterCall.AccountBalance);
        //TODO parse xmlDoc for wallet balance
        return 0L;
    }
    
    public Skill getSkillInTraining() {
        Document xmlDoc = caller.getCharacterDocument(character, CharacterCall.SkillInTraining);
        //TODO parse xmlDoc for skill in training
        return null;
    }
    
    public Skill getSkillQueue() {
        Document xmlDoc = caller.getCharacterDocument(character, CharacterCall.SkillQueue);
        //TODO parse xmlDoc for skill in training
        return null;
    }
    
    public boolean isTranquilityOnline() {
        Document xmlDoc = caller.getServerDocument(ServerCall.ServerStatus);
        //TODO parse xmlDoc for server status
        return false;
    }
    
    public int getActivePlayerCount() {
        Document xmlDoc = caller.getServerDocument(ServerCall.ServerStatus);
        //TODO parse xmlDoc for active player count;
        return 0;
    }
}
