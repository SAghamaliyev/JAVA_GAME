package items;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public abstract class Item {
    protected String name;                 // Item name (Apple, Key, Potion etc.)
    protected BufferedImage icon;        // Sprite(icon) for UI and inventory
    protected boolean consumable = false;  // If true item will be removed after use
    
    public Item(String name) {
        this.name = name;
    }

    // Called when player uses the item
    public abstract void use();

    // Draw item in inventory or UI
    public void draw(Graphics2D g2, int x, int y, int size) {
        if (icon != null) {
            g2.drawImage(icon, x, y, size, size, null);
        }
    }

    // Getters
    public String getName() {
        return name;
    }

    public BufferedImage getIcon() {
        return icon;
    }

    // Boolean checkers
    public boolean isConsumable() {
        return consumable;
    }
}
