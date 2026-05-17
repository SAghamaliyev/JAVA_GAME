package items;

import graphics.Assets;

public class Key extends Item {

    private int keyId; 

    public Key(int keyId) {
        super("Key");
        this.keyId = keyId;
        this.icon = Assets.keyIcon;
    }
    
    public int getId() {
        return keyId;
    }
}
