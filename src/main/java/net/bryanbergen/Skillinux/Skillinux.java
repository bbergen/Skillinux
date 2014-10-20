package net.bryanbergen.Skillinux;

import net.bryanbergen.Skillinux.Database.DatabaseConnection;

public class Skillinux {

    public static void main(String[] args) {
        
        System.out.println(DatabaseConnection.getInstance().getSkill(2505));
    }
}
