package net.bryanbergen.Skillinux.XMLParser;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.bryanbergen.Skillinux.APICall.AccountCall;
import net.bryanbergen.Skillinux.APICall.ApiCall;
import net.bryanbergen.Skillinux.APICall.CharacterCall;
import net.bryanbergen.Skillinux.APICall.ServerCall;
import net.bryanbergen.Skillinux.Database.DatabaseConnection;
import net.bryanbergen.Skillinux.Entities.API;
import net.bryanbergen.Skillinux.Entities.EveCharacter;
import net.bryanbergen.Skillinux.Entities.Skill;
import net.bryanbergen.Skillinux.Util.CalendarUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Parser {

    private static final String ONLINE_PLAYERS = "onlinePlayers";
    private static final String SERVER_OPEN = "serverOpen";
    private static final String BALANCE = "balance";
    private static final String CACHE_TIME = "cacheTime";
    private static final String WALLET_BALANCE = "walletBalance";
    
    private final ApiCall caller = ApiCall.getInstance();
    private API api;
    private EveCharacter character;
    private Map<String, String> serverCache;
    private Map<String, String> characterCache;
    private Map<String, String> accountCache;
    private Map<String, Map<String, String>> cachePool;
    
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
    
    ////////////////////////////////////////////////////////////////////////////
    /////                     ACCOUNT CALL API METHODS                     /////
    ////////////////////////////////////////////////////////////////////////////
    
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

    ////////////////////////////////////////////////////////////////////////////
    /////                   CHARACTER CALL API METHODS                     /////
    ////////////////////////////////////////////////////////////////////////////
    
    /**
     * Returns the raw balance of an <code>EveCharacter</code>'s wallet balance.
     * Throws <code>IllegalStateException</code> if <code>Parser</code> not 
     * created with an <code>EveCharacter</code>. 
     * 
     * @return Wallet balance.
     */
    public double getWalletBalance() {
        if (character == null) {
            throw new IllegalStateException("Parser must be created with an Eve Character for CharacterCalls.");
        }
        if (refreshCharacterCache(CharacterCall.AccountBalance)) {
            cacheWalletBalance();
        }
        return Double.parseDouble(characterCache.get(WALLET_BALANCE));
    }
    
    /**
     * Returns the balance of an <code>EveCharacter</code>'s wallet balance.
     * 
     * @return Wallet balance as a formatted String.
     */
    public String getFormattedWalletBalance() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        return formatter.format(getWalletBalance());
    }
    
    public Skill getSkillInTraining() {
        Document xmlDoc = caller.getCharacterDocument(character, CharacterCall.SkillInTraining);
        NodeList nodes = xmlDoc.getDocumentElement().getChildNodes();
        
        Map<String, Node> nodeMap = getNodesByName(nodes,
                "trainingEndTime",
                "trainingStartTime",
                "trainingTypeID",
                "trainingStartSP",
                "trainingDestinationSP",
                "trainingToLevel");
        
        // Fetch Cooresponding skill from database
        int typeId = Integer.parseInt(getValueFromNode(nodeMap.get("trainingTypeID")));
        Skill skill = DatabaseConnection.getInstance().getSkill(typeId);
        
        // Populate Skill based on xml
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Node trainingStart = nodeMap.get("trainingStartTime");
        skill.setTrainingStartTime(CalendarUtil.getDateFromString(getValueFromNode(trainingStart), format));
        Node trainingEnd = nodeMap.get("trainingEndTime");
        skill.setTrainingEndTime(CalendarUtil.getDateFromString(getValueFromNode(trainingEnd), format));
        Node trainingStartSP = nodeMap.get("trainingStartSP");
        skill.setTrainingStartSP(Integer.parseInt(getValueFromNode(trainingStartSP)));
        Node trainingDestinationSP = nodeMap.get("trainingDestinationSP");
        skill.setTrainingDestinationSP(Integer.parseInt(getValueFromNode(trainingDestinationSP)));
        Node trainingToLevel = nodeMap.get("trainingToLevel");
        skill.setTrainingToLevel(Integer.parseInt(getValueFromNode(trainingToLevel)));

        return skill;
    }
    
    public Skill getSkillQueue() {
        Document xmlDoc = caller.getCharacterDocument(character, CharacterCall.SkillQueue);
        //TODO parse xmlDoc for skill in training
        return null;
    }
    
    ////////////////////////////////////////////////////////////////////////////
    /////                     SERVER CALL API METHODS                      /////
    ////////////////////////////////////////////////////////////////////////////

    /**
     * Query the API server for the status of Tranquility
     * 
     * @return True if Tranquility is online, false otherwise. 
     */
    public boolean isTranquilityOnline() {
        if (updateServerCache(ServerCall.ServerStatus)) {
            cacheServerStatus();
        }
        return Boolean.valueOf(serverCache.get(SERVER_OPEN));
    }
    
    /**
     * Query the API server to get the active player count.
     * 
     * @return Number of players online on Tranquility.
     */
    public int getActivePlayerCount() {
        if (updateServerCache(ServerCall.ServerStatus)) {
            cacheServerStatus();
        }
        return Integer.parseInt(serverCache.get(ONLINE_PLAYERS));
    }
    
    ////////////////////////////////////////////////////////////////////////////
    /////                       END OF API METHODS                         /////
    ////////////////////////////////////////////////////////////////////////////
    
    private void cacheWalletBalance() {
        Document xmlDoc = caller.getCharacterDocument(character, CharacterCall.AccountBalance);
        String value = getAttributeValueByName(xmlDoc.getChildNodes(), BALANCE);
        
        if (characterCache == null) {
            characterCache = new HashMap<String, String>();
        }
        
        characterCache.put(CACHE_TIME, System.currentTimeMillis() + "");
        characterCache.put(WALLET_BALANCE, value);
    }
    
    private void cacheServerStatus() {
        Document xmlDoc = caller.getServerDocument(ServerCall.ServerStatus);
        NodeList nodes = xmlDoc.getDocumentElement().getChildNodes();
        Node playerCount = getNodeByName(nodes, ONLINE_PLAYERS);
        Node serverOpen = getNodeByName(nodes, SERVER_OPEN);
        
        if (serverCache == null) {
            serverCache = new HashMap<String, String>();
        }
        
        serverCache.put(CACHE_TIME, System.currentTimeMillis() + "");
        serverCache.put(ONLINE_PLAYERS, playerCount.getLastChild().getTextContent());
        serverCache.put(SERVER_OPEN, serverOpen.getLastChild().getTextContent());
    }
    
    private boolean updateServerCache(ServerCall callType) {
        if (serverCache == null) {
            return true;
        }
        long lastCache = Long.parseLong(serverCache.get(CACHE_TIME));
        long now = System.currentTimeMillis();
        return now - lastCache > callType.getCacheTime();
    }
    
    private boolean refreshCharacterCache(CharacterCall callType) {
        if (characterCache == null) {
            return true;
        }
        long lastCache = Long.parseLong(characterCache.get(CACHE_TIME));
        long now = System.currentTimeMillis();
        return now - lastCache > callType.getCacheTime();
    }
    
    private boolean refreshAccountCache(AccountCall callType) {
        if (accountCache == null) {
            return true;
        }
        long lastCache = Long.parseLong(accountCache.get(CACHE_TIME));
        long now = System.currentTimeMillis();
        return now - lastCache > callType.getCacheTime();
    }
    
    /**
     * Convenience method for getting shallow nodes from NodeLists.
     * <b>This method will not search deeper than one Node</b>.
     * 
     * @param nodes Found by <code>getDocumentElement().getChildNodes()</code>
     * @param name Search parameter
     * @return Corresponding <code>Node</code> if found, null otherwise.
     */
    private Node getNodeByName(NodeList nodes, String name) {
        Node n = null;
        outerloop:
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node instanceof Element && node.hasChildNodes()) {
                NodeList children = node.getChildNodes();
                for (int k = 0; k < children.getLength(); k++) {
                    Node child = children.item(k);
                    if (child instanceof Element) {
                        if (child.getNodeName().equals(name)) {
                            n = child;
                            break outerloop;
                        }
                    }
                }
            }
        }
        return n;
    }
    
    private Map<String, Node> getNodesByName(NodeList nodes, String... keys) {
        Map<String, Node> nodeMap = new HashMap<String, Node>();
        for (String key : keys) {
            nodeMap.put(key, getNodeByName(nodes, key));
        }
        return nodeMap;
    }
    
    private String getAttributeValueByName(NodeList nodeList, String name) {
        String value = "";
        outerloop:
        for (int i = 0; i < nodeList.getLength(); i++) {
            if (!value.equals("")) {
                break;
            }
            Node tempNode = nodeList.item(i);
            if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
                if (tempNode.hasAttributes()) {
                    NamedNodeMap nodeMap = tempNode.getAttributes();
                    for (int k = 0; k < nodeMap.getLength(); k++) {
                        Node node = nodeMap.item(k);
                        if (node.getNodeName().equals(name)) {
                            value = node.getNodeValue();
                            break outerloop;
                        }
                    }
                }
                if (tempNode.hasChildNodes()) { 
                    value = getAttributeValueByName(tempNode.getChildNodes(), name);
                }
            }
        }
        return value;
    }
    
    private String getValueFromNode(Node node) {
        return node.getLastChild().getTextContent();
    }
    
    
    /**
     * Debugging method for examining the nodes of an XMLDocument. 
     * This method will recursively traverse the <code>NodeList</code>
     * 
     * @param nodeList 
     */
    private void printOutXML(NodeList nodeList) {
        for (int count = 0; count < nodeList.getLength(); count++) {
            Node tempNode = nodeList.item(count);
            if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
                System.out.println("\nNode Name = " + tempNode.getNodeName() + " [OPEN]");
                System.out.println("Node Value = " + tempNode.getTextContent());
                if (tempNode.hasAttributes()) {
                    NamedNodeMap nodeMap = tempNode.getAttributes();
                    for (int i = 0; i < nodeMap.getLength(); i++) {
                        Node node = nodeMap.item(i);
                        System.out.println("Attribute name : " + node.getNodeName());
                        System.out.println("Attribute value : " + node.getNodeValue());
                    }
                }
                if (tempNode.hasChildNodes()) {
                    printOutXML(tempNode.getChildNodes());
                }
                System.out.println("Node Name = " + tempNode.getNodeName() + " [CLOSE]");
            }
        }
    }
}
