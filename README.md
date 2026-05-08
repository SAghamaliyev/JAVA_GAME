# 🎮 Java Dungeon Game

A 2D dungeon crawler game built with Java. Navigate through procedurally structured dungeons, battle enemies, collect items, and find your way to freedom.

---

## 📖 Table of Contents

- [About](#about)
- [Features](#features)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Controls](#controls)

---

## About

Java Dungeon Game is a tile-based dungeon crawler where the player explores rooms, fights enemies, loots chests, and manages an inventory of items such as keys and crowbars. The game features a heads-up display (HUD), an inventory UI, and a main menu — all built on top of a Java Swing `GamePanel`.

---

## Features

- 🗺️ **Dungeon exploration** — move between interconnected rooms inside a dungeon
- ⚔️ **Enemies** — encounter and defeat enemies that roam the dungeon
- 🎒 **Inventory system** — collect and manage items (keys, crowbars, food)
- 🔑 **Doors & Chests** — unlock doors with keys and open chests for loot
- 🍖 **Food** — pick up food to restore health
- 🖥️ **HUD** — on-screen display showing player stats
- 🎵 **Sound & Sprites** — audio effects and sprite-based graphics
- ⌨️ **Keyboard input** — responsive keyboard-driven controls

---

## Project Structure

```
src/
│
├── main/
│   ├── Main.java             # Entry point — launches the game window
│   ├── GamePanel.java        # Core game loop and rendering (paintComponent)
│   └── GameState.java        # Enum: MENU, PLAYING, PAUSED, GAME_OVER
│
├── entities/
│   ├── Entity.java           # Abstract base — x, y, width, height, BufferedImage sprite
│   ├── Player.java           # Movement, interaction, inventory reference
│   ├── Enemy.java            # AI, patrol, attack logic
│   ├── Chest.java            # Lootable chest; holds Item list; draws chest sprite
│   └── Door.java             # Locked door; unlock(Key); draws door sprite
│
├── items/
│   ├── Item.java             # Abstract base — name, BufferedImage icon, use()
│   │   ├── Food.java         # Abstract — healAmount; eat() restores HP
│   │   │   ├── Apple.java    # +5 HP
│   │   │   ├── Cake.java     # +20 HP
│   │   │   └── Potion.java   # +15 HP, cures debuffs
│   │   ├── Key.java          # Opens Door entities
│   │   └── Crowbar.java      # Forces open Chest entities
│
├── world/
│   ├── Room.java             # Tile grid, exits, list of entities in room
│   ├── Dungeon.java          # Room[][]; generates layout; tracks current room
│   └── TileManager.java      # Loads tileset PNG; draws tiles via g2.drawImage()
│
├── ui/
│   ├── Menu.java             # Main menu screen; draws buttons
│   ├── HUD.java              # HP bar, key count, active item icon
│   └── InventoryUI.java      # Grid overlay; renders Item icons from Inventory
│
├── input/
│   └── KeyboardHandler.java  # KeyListener; boolean map keyPressed[]
│
├── managers/
│   ├── EntityManager.java    # List<Entity>; update() and draw() all entities
│   ├── CollisionManager.java # Tile collision + entity–entity overlap checks
│   └── Inventory.java        # Slots[], add/remove/get; used by Player
│
└── utils/
    └── Constants.java        # TILE_SIZE, SCREEN_COLS, SCREEN_ROWS, FPS, SCALE
```

---

## Prerequisites

- **Java 17** or higher ([Download](https://adoptium.net/))
- A Java IDE such as [IntelliJ IDEA](https://www.jetbrains.com/idea/) or [Eclipse](https://www.eclipse.org/), or the JDK command-line tools

---

## Getting Started

### Compile

```bash
javac -d out -sourcepath src src/main/Main.java
```

### Run

```bash
java -cp out main.Main
```

> **Tip:** If you are using an IDE, simply import the project, set `Main.java` as the run configuration entry point, and hit **Run**.

---

## Controls

| Key | Action |
|-----|--------|
| `W` / `↑` | Move up |
| `S` / `↓` | Move down |
| `A` / `←` | Move left |
| `D` / `→` | Move right |
| `E` | Interact (open door / chest) |
| `I` | Toggle inventory |
| `Esc` | Pause / open menu |
