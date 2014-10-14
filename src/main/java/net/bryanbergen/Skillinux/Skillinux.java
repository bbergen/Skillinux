package net.bryanbergen.Skillinux;

import java.util.List;
import net.bryanbergen.Skillinux.Database.DatabaseConnection;
import net.bryanbergen.Skillinux.Entities.EveCharacter;

public class Skillinux {

    public static void main(String[] args) {
        
        List<EveCharacter> characters = DatabaseConnection.getInstance().loadAllCharacters();
        
        for (EveCharacter c : characters) {
            System.out.println(c);
        }
    }
}
