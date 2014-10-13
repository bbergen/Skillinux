package net.bryanbergen.Skillinux.Entities;

public class DemoCharacter extends EveCharacter {

    public DemoCharacter() {
        setName("Magnus Orin (Demo)");
        setApi(new DemoAPI());
        setCharacterID(1179439547);
    }
}
