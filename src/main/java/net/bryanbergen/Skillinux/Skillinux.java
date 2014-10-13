package net.bryanbergen.Skillinux;

import java.util.List;
import net.bryanbergen.Skillinux.Database.DatabaseConnection;
import net.bryanbergen.Skillinux.Entities.API;

public class Skillinux {

    public static void main(String[] args) {

        List<API> keys = DatabaseConnection.getInstance().loadAPIs();
        
        for (API key : keys) {
            System.out.println(key);
        }
    }
}
