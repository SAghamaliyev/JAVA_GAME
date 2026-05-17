package items;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public abstract class Item {
    protected String name;                 
    protected BufferedImage icon;        
    protected boolean consumable = false;  
    
    public Item(String name) {
        this.name = name;
    }
    
    public void draw(Graphics2D g2, int x, int y, int size) {
        if (icon != null) {
            g2.drawImage(icon, x, y, size, size, null);
        }
    }

    
    public String getName() {
        return name;
    }

    public BufferedImage getIcon() {
        return icon;
    }

    
    public boolean isConsumable() {
        return consumable;
    }
}
