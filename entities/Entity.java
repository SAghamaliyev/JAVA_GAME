package entities;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public abstract class Entity {  // It is abstract because we will extend it

    // We use protected to allow access in subclasses (inheritance)
    // Even if they are in different packages.
    protected int x, y;
    protected int width, height;
    protected BufferedImage sprite;  // Picture of object(how it will be drawn in the game)
    protected boolean active = true; // if false then EntityManager removes it

    // Constructor
    public Entity(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public abstract void update();  // Updated information about logic/movements/animation of object
    public abstract void draw(Graphics2D g2);  // It loads its sprite from spriteloader and draws itself

    // Axis-aligned bounding box collision
    public boolean collidesWith(Entity other) {
        return x < other.x + other.width
            && x + width > other.x
            && y < other.y + other.height
            && y + height > other.y;
    }

    public boolean isActive() { return active; }  // if it is not active EntityMangaer will delete current object

    // Setters
    public void setActive(boolean active) { this.active = active; }

    // Getters
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}
