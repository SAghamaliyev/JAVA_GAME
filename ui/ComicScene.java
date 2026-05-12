package ui;

import graphics.Assets;
import input.KeyboardHandler;
import utils.Constants;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.image.BufferedImage;

public class ComicScene {

    private KeyboardHandler keyHandler;
    private int currentPage = 0;
    private boolean keyReleased = true;
    private boolean finished = false;

    public ComicScene(KeyboardHandler keyHandler) {
        this.keyHandler = keyHandler;
    }

    public void update() {
        if (finished) return;

        // Check for advance (Enter or Space)
        if (keyHandler.enter || keyHandler.attack) {
            if (keyReleased) {
                currentPage++;
                keyReleased = false;
                if (currentPage >= Assets.comicPages.length) {
                    finished = true;
                }
            }
        } else {
            keyReleased = true;
        }
    }

    public void draw(Graphics2D g2) {
        if (finished || currentPage >= Assets.comicPages.length) return;

        BufferedImage img = Assets.comicPages[currentPage];
        if (img != null) {
            // Scale image to fit screen while maintaining aspect ratio if possible, 
            // but for simplicity we'll just fill the screen or center it.
            int screenW = Constants.SCREEN_WIDTH;
            int screenH = Constants.SCREEN_HEIGHT;
            
            // Center the image
            double imgAspect = (double) img.getWidth() / img.getHeight();
            double screenAspect = (double) screenW / screenH;
            
            int drawW, drawH;
            if (imgAspect > screenAspect) {
                drawW = screenW;
                drawH = (int) (screenW / imgAspect);
            } else {
                drawH = screenH;
                drawW = (int) (screenH * imgAspect);
            }
            
            int x = (screenW - drawW) / 2;
            int y = (screenH - drawH) / 2;
            
            g2.drawImage(img, x, y, drawW, drawH, null);
        }

        // Draw "Press ENTER to continue" at the bottom
        // g2.setFont(new Font("SansSerif", Font.BOLD, 20));
        // String text = "Press ENTER to continue (" + (currentPage + 1) + "/" + Assets.comicPages.length + ")";
        
        // Add a subtle shadow/background for readability
        // FontMetrics fm = g2.getFontMetrics();
        // int tx = (Constants.SCREEN_WIDTH - fm.stringWidth(text)) / 2;
        // int ty = Constants.SCREEN_HEIGHT - 30;
        
        // g2.setColor(new Color(0, 0, 0, 150));
        // g2.fillRect(tx - 5, ty - fm.getAscent() - 2, fm.stringWidth(text) + 10, fm.getHeight() + 4);
        
        // g2.setColor(Color.WHITE);
        // g2.drawString(text, tx, ty);
    }

    public boolean isFinished() {
        return finished;
    }

    public void reset() {
        currentPage = 0;
        finished = false;
        keyReleased = false; // Prevent immediate skip if enter was pressed in menu
    }
}
