package net.bryanbergen.Skillinux;

import net.bryanbergen.Skillinux.Database.DatabaseConnection;
import net.bryanbergen.Skillinux.Entities.DemoAPI;
import net.bryanbergen.Skillinux.Entities.DemoCharacter;

public class Skillinux {

    public static void main(String[] args) {

        DatabaseConnection.getInstance().saveAPI(new DemoAPI());
        DatabaseConnection.getInstance().saveCharacter(new DemoCharacter());
    }
}
