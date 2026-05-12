package main;

import entities.*;
import items.*;
import managers.*;
import input.KeyboardHandler;
import graphics.Assets;
import world.*;
import ui.HUD;
import ui.InventoryUI;
import ui.ComicScene;
import utils.Constants;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {

    private Thread gameThread;
    private GameState gameState;
    private KeyboardHandler keyHandler;

    private Player player;
    private Dungeon dungeon;
    private TileManager currentTileManager;
    private Room currentRoom;
    private EntityManager entityManager;
    private AchievementManager achievementManager;

    private ui.Menu menu;
    private HUD hud;
    private InventoryUI inventoryUI;
    private ComicScene comicScene;

    private boolean pauseReleased = true;
    private CopyOnWriteArrayList<Bullet> bullets;
    private int totalAchievements;

    public GamePanel() {
        setPreferredSize(new Dimension(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT));
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
        setFocusable(true);

        keyHandler = new KeyboardHandler();
        addKeyListener(keyHandler);

        Assets.load();

        gameState = GameState.MENU;
        menu = new ui.Menu(keyHandler);
        comicScene = new ComicScene(keyHandler);
        bullets = new CopyOnWriteArrayList<>();
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    private void initGame() {
        player = new Player(100, Constants.SCREEN_HEIGHT / 2, Constants.PLAYER_SIZE, Constants.PLAYER_SIZE);
        player.setKeyboardHandler(keyHandler);

        dungeon = new Dungeon(player);
        entityManager = new EntityManager();
        achievementManager = new AchievementManager();
        bullets.clear();

        totalAchievements = 0;
        for (Room r : dungeon.getRooms()) {
            for (Entity e : r.getItems()) {
                if (e instanceof Achievement) {
                    achievementManager.addAchievement((Achievement) e);
                    totalAchievements++;
                }
            }
        }

        loadRoom(dungeon.getStartRoom());
        hud = new HUD(player);
        inventoryUI = new InventoryUI(player, keyHandler);
    }

    private void loadRoom(Room room) {
        currentRoom = room;
        currentTileManager = dungeon.getTileManager(room.getId());
        entityManager.clear();
        bullets.clear();

        for (Entity e : room.getAllEntities()) {
            entityManager.addEntity(e);
        }
    }

    @Override
    public void run() {
        double drawInterval = 1_000_000_000.0 / Constants.FPS;
        double delta = 0;
        long lastTime = System.nanoTime();

        while (gameThread != null) {
            long currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }

            try { Thread.sleep(1); } catch (InterruptedException ignored) {}
        }
    }

    private void update() {
        switch (gameState) {
            case MENU:
                String result = menu.update();
                if (result.equals("PLAYING")) { 
                    comicScene.reset();
                    gameState = GameState.STORY; 
                }
                break;
            case STORY:
                comicScene.update();
                if (comicScene.isFinished()) {
                    initGame();
                    gameState = GameState.PLAYING;
                }
                break;
            case PLAYING:
                updatePlaying();
                break;
            case PAUSED:
                updatePaused();
                break;
            case GAME_OVER:
            case WIN:
                if (keyHandler.enter) { gameState = GameState.MENU; menu.reset(); }
                break;
        }
    }

    private void updatePlaying() {
        
        if (!keyHandler.pause) pauseReleased = true;
        if (keyHandler.pause && pauseReleased) { gameState = GameState.PAUSED; pauseReleased = false; return; }

        
        inventoryUI.update();
        handleItemUse();

        
        player.update();

        
        handleTileCollision(player);

        
        entityManager.update();

        
        List<Bullet> deadBullets = new ArrayList<>();
        for (Bullet b : bullets) {
            b.update();
            if (!b.isActive()) deadBullets.add(b);
        }
        bullets.removeAll(deadBullets);

        
        List<Enemy> enemies = entityManager.getEntitiesOfType(Enemy.class);
        List<Door> doors = entityManager.getEntitiesOfType(Door.class);

        CollisionManager.checkPlayerEnemyCollision(player, enemies);
        CollisionManager.checkPlayerDoorCollision(player, doors);
        CollisionManager.checkBulletEnemyCollision(bullets, enemies);
        CollisionManager.clampToRoom(player, 0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);

        
        for (Enemy e : enemies) {
            if (e.isActive()) handleTileCollision(e);
        }

        
        handleInteraction();

        
        handleMeleeAttack(enemies);

        
        handleDoorTransition(doors);

        
        handleEnemyDeaths(enemies);

        
        if (!player.isAlive()) {
            player.respawn();
            loadRoom(dungeon.getRoom(player.getSpawnRoomId()));
            inventoryUI.showMessage("You died! Respawning...");
        }

        
        if (currentRoom.getId().equals("room5")) {
            boolean allDead = true;
            for (Enemy e : enemies) { if (e.isActive()) { allDead = false; break; } }
            if (allDead && !enemies.isEmpty()) gameState = GameState.WIN;
        }
    }

    private void handleTileCollision(LivingBeing being) {
        if (currentTileManager == null) return;
        int ts = Constants.TILE_SIZE;
        int bx = being.getX(), by = being.getY(), bw = being.getWidth(), bh = being.getHeight();

        
        if (currentTileManager.isSolidAtPixel(bx, by) || currentTileManager.isSolidAtPixel(bx + bw - 1, by)
            || currentTileManager.isSolidAtPixel(bx, by + bh - 1) || currentTileManager.isSolidAtPixel(bx + bw - 1, by + bh - 1)) {
            
            int col = (bx + bw / 2) / ts;
            int row = (by + bh / 2) / ts;
            int safeX = col * ts + (ts - bw) / 2;
            int safeY = row * ts + (ts - bh) / 2;
            being.setPosition(safeX, safeY);
        }
    }

    private void handleInteraction() {
        if (!keyHandler.interact) return;

        
        for (Entity e : new ArrayList<>(entityManager.getEntities())) {
            if (!e.isActive()) continue;

            
            int range = 10;
            boolean inRange = player.getX() - range < e.getX() + e.getWidth() &&
                              player.getX() + player.getWidth() + range > e.getX() &&
                              player.getY() - range < e.getY() + e.getHeight() &&
                              player.getY() + player.getHeight() + range > e.getY();
            
            if (!inRange) continue;

            if (e instanceof Chest && !((Chest)e).isBroken()) {
                Chest chest = (Chest) e;
                if (player.getInventory().has("Crowbar")) {
                    var loot = chest.breakChest();
                    if (loot != null) for (var item : loot) player.getInventory().add(item);
                    inventoryUI.showMessage("Chest opened!");
                } else {
                    inventoryUI.showMessage("You need a Crowbar!");
                }
            }

            if (e instanceof Barrel && !((Barrel)e).isActivated()) {
                Barrel barrel = (Barrel) e;
                var loot = barrel.breakBarrel();
                if (loot != null) for (var item : loot) player.getInventory().add(item);
                inventoryUI.showMessage("Barrel smashed!");
            }

            if (e instanceof Door && ((Door)e).isLocked()) {
                Door door = (Door) e;
                Key key = (Key) player.getInventory().get("Key");
                if (key != null && key.getId() == door.getDoorId()) {
                    player.getInventory().remove(key);
                    door.unlock(key);
                    inventoryUI.showMessage("Door unlocked!");
                } else {
                    inventoryUI.showMessage("You need a key!");
                }
            }
        }
    }

    private void handleMeleeAttack(List<Enemy> enemies) {
        if (!keyHandler.attack) return;

        Item weapon = player.getEquippedWeapon();
        if (weapon instanceof Gun) {
            Gun gun = (Gun) weapon;
            if (gun.canFire()) {
                gun.fire();
                fireBullet();
                inventoryUI.showMessage("Fired!");
            }
            return;
        }

        if (!player.canAttack()) return;
        player.performAttack();

        for (Enemy e : enemies) {
            if (!e.isActive()) continue;
            int dx = Math.abs(e.getX() - player.getX());
            int dy = Math.abs(e.getY() - player.getY());
            if (dx < Constants.ATTACK_RANGE && dy < Constants.ATTACK_RANGE) {
                player.attack(e);
                inventoryUI.showMessage("Hit! -" + player.getStrength() + " HP");
            }
        }
    }

    private void handleDoorTransition(List<Door> doors) {
        for (Door door : doors) {
            if (door.isLocked() || !door.isActive()) continue;
            if (!player.collidesWith(door)) continue;
            if (door.getTargetRoomId() != null) {
                Room next = dungeon.getRoom(door.getTargetRoomId());
                if (next != null) {
                    player.setPosition(door.getTargetX(), door.getTargetY());
                    loadRoom(next);
                    inventoryUI.showMessage("Entered " + next.getId());
                    return;
                }
            }
        }
    }

    private void handleEnemyDeaths(List<Enemy> enemies) {
        for (Enemy e : enemies) {
            if (!e.isAlive()) {
                List<Item> loot = e.dropItem();
                for (Item item : loot) {
                    player.getInventory().add(item);
                    inventoryUI.showMessage("Looted: " + item.getName());
                }
            }
        }
    }

    private void handleItemUse() {
        int slot = inventoryUI.getSelectedSlot();
        if (slot < 0) return;
        inventoryUI.clearSelectedSlot();

        List<Item> items = player.getInventory().getSlots();
        if (slot >= items.size()) return;

        Item item = items.get(slot);
        if (item instanceof Food) {
            player.useFood((Food) item);
            inventoryUI.showMessage("Used " + item.getName() + " (+" + ((Food)item).getHealAmount() + " HP)");
            if (item.isConsumable()) player.getInventory().remove(item);
        } else if (item instanceof Gun) {
            if (player.getEquippedWeapon() == item) {
                player.setEquippedWeapon(null);
                inventoryUI.showMessage("Unequipped Gun");
            } else {
                player.setEquippedWeapon(item);
                inventoryUI.showMessage("Equipped Gun");
            }
        } else {
            inventoryUI.showMessage(item.getName() + " - can't use directly");
        }
    }

    private void fireBullet() {
        int dx = 0, dy = 0;
        switch (player.getDirection()) {
            case 0: dy = -Constants.BULLET_SPEED; break;
            case 1: dy =  Constants.BULLET_SPEED; break;
            case 2: dx = -Constants.BULLET_SPEED; break;
            case 3: dx =  Constants.BULLET_SPEED; break;
        }
        Bullet b = new Bullet(
            player.getX() + player.getWidth() / 2 - Constants.BULLET_SIZE / 2,
            player.getY() + player.getHeight() / 2 - Constants.BULLET_SIZE / 2,
            dx, dy
        );
        bullets.add(b);
    }

    private void updatePaused() {
        if (!keyHandler.pause) pauseReleased = true;
        if (keyHandler.pause && pauseReleased) { gameState = GameState.PLAYING; pauseReleased = false; }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        switch (gameState) {
            case MENU:
                menu.draw(g2);
                break;
            case STORY:
                comicScene.draw(g2);
                break;
            case PLAYING:
                drawPlaying(g2);
                break;
            case PAUSED:
                drawPlaying(g2);
                drawOverlay(g2, "PAUSED", "Press ESC to resume", new Color(50, 50, 100, 180));
                break;
            case GAME_OVER:
                drawOverlay(g2, "GAME OVER", "Press ENTER for menu", new Color(100, 20, 20, 200));
                break;
            case WIN:
                drawPlaying(g2);
                drawOverlay(g2, "YOU WIN!", "The dungeon is conquered! Press ENTER.", new Color(20, 80, 40, 200));
                break;
        }

        g2.dispose();
    }

    private void drawPlaying(Graphics2D g2) {
        if (currentTileManager == null || entityManager == null || player == null) return;
        currentTileManager.draw(g2);
        entityManager.draw(g2);
        for (Bullet b : bullets) b.draw(g2);
        player.draw(g2);
        if (hud != null) hud.draw(g2, currentRoom.getId(), achievementManager.getUnlockedCount(), totalAchievements);
        if (inventoryUI != null) inventoryUI.draw(g2);
    }

    private void drawOverlay(Graphics2D g2, String title, String subtitle, Color bgColor) {
        int w = Constants.SCREEN_WIDTH, h = Constants.SCREEN_HEIGHT;
        g2.setColor(bgColor);
        g2.fillRect(0, 0, w, h);

        g2.setFont(new Font("SansSerif", Font.BOLD, 60));
        g2.setColor(Color.WHITE);
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(title, (w - fm.stringWidth(title)) / 2, h / 2 - 20);

        g2.setFont(new Font("SansSerif", Font.PLAIN, 20));
        g2.setColor(new Color(220, 220, 220));
        fm = g2.getFontMetrics();
        g2.drawString(subtitle, (w - fm.stringWidth(subtitle)) / 2, h / 2 + 30);
    }
}
