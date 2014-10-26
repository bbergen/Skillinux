package net.bryanbergen.Skillinux;

import net.bryanbergen.Skillinux.Entities.DemoCharacter;
import net.bryanbergen.Skillinux.XMLParser.Parser;

public class Skillinux {

    public static void main(String[] args) {
        
        Parser parser = new Parser(new DemoCharacter());
        System.out.println(parser.getFormattedWalletBalance());
        System.out.println(parser.getSkillInTraining());
    }
}
