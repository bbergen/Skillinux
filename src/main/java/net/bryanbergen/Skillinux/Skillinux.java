package net.bryanbergen.Skillinux;

import net.bryanbergen.Skillinux.XMLParser.Parser;

public class Skillinux {

    public static void main(String[] args) {
        Parser parser = new Parser();
        boolean online = parser.isTranquilityOnline();
        int playerCount = parser.getActivePlayerCount();
        System.out.println("Tranquility is " + (online ? "online with " + playerCount + " players." : "offline."));
    }
}
