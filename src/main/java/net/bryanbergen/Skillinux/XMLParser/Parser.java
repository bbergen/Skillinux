package net.bryanbergen.Skillinux.XMLParser;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.bryanbergen.Skillinux.APICall.AccountCall;
import net.bryanbergen.Skillinux.APICall.ApiCall;
import net.bryanbergen.Skillinux.APICall.CharacterCall;
import net.bryanbergen.Skillinux.APICall.ServerCall;
import net.bryanbergen.Skillinux.Entities.API;
import net.bryanbergen.Skillinux.Entities.EveCharacter;
import net.bryanbergen.Skillinux.Entities.Skill;
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
    private static final String CURRENT_TIME = "currentTime";
    
    private final ApiCall caller = ApiCall.getInstance();
    private API api;
    private EveCharacter character;
    private Map<String, String> serverQueryResults;
    private boolean serverStatusCached;
    
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
        Document xmlDoc = caller.getCharacterDocument(character, CharacterCall.AccountBalance);
        String value = getAttributeValueByName(xmlDoc.getChildNodes(), BALANCE);
        return Double.parseDouble(value);
    }
    
    public String getFormattedWalletBalance() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        return formatter.format(getWalletBalance());
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
    
    /**
     * Query the API server for the status of Tranquility
     * 
     * @return True if Tranquility is online, false otherwise. 
     */
    public boolean isTranquilityOnline() {
        if (updateServerQueryCache()) {
            cacheServerStatus();
        }
        return Boolean.valueOf(serverQueryResults.get(SERVER_OPEN));
    }
    
    /**
     * Query the API server to get the active player count.
     * 
     * @return Number of players online on Tranquility.
     */
    public int getActivePlayerCount() {
        if (updateServerQueryCache()) {
            cacheServerStatus();
        }
        return Integer.parseInt(serverQueryResults.get(ONLINE_PLAYERS));
    }
    
    private void cacheServerStatus() {
        Document xmlDoc = caller.getServerDocument(ServerCall.ServerStatus);
        NodeList nodes = xmlDoc.getDocumentElement().getChildNodes();
        Node playerCount = getNodeByName(nodes, ONLINE_PLAYERS);
        Node serverOpen = getNodeByName(nodes, SERVER_OPEN);
        
        if (serverQueryResults == null) {
            serverQueryResults = new HashMap<String, String>();
        }
        
        serverQueryResults.put(CURRENT_TIME, Calendar.getInstance().getTimeInMillis() + "");
        serverQueryResults.put(ONLINE_PLAYERS, playerCount.getLastChild().getTextContent());
        serverQueryResults.put(SERVER_OPEN, serverOpen.getLastChild().getTextContent());
        serverStatusCached = true;
    }
    
    private boolean updateServerQueryCache() {
        //TODO Flesh this out so it tracks the time the query was cached, and checks if it should be refreshed.
        return !serverStatusCached;
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
