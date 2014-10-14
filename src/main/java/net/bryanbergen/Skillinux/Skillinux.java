package net.bryanbergen.Skillinux;

import net.bryanbergen.Skillinux.XMLParser.Parser;

public class Skillinux {

    public static void main(String[] args) {
        System.out.println(new Parser().getActivePlayerCount());
    }
}
