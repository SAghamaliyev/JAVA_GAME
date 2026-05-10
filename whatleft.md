Based on the task division and README:

### Person 3 — Items (missing)

| Class          | What it does                                   |
| -------------- | ---------------------------------------------- |
| `Gun`          | New item type                                  |
| `Barrel`       | Implements `Activatable`, stores items         |
| Wiring `use()` | Actual logic inside each item's `use()` method |

### Engine / Shared (missing — not assigned to any person)

| Class                   | What it does                     |
| ----------------------- | -------------------------------- |
| `Main.java`             | Entry point                      |
| `GamePanel.java`        | Game loop, update/render         |
| `GameState.java`        | MENU, PLAYING, PAUSED, GAME_OVER |
| `Dungeon.java`          | Room collection, map generation  |
| `TileManager.java`      | Tile loading and rendering       |
| `Menu.java`             | Main menu UI                     |
| `HUD.java`              | Health bar, key count            |
| `InventoryUI.java`      | Inventory grid display           |
| `KeyboardHandler.java`  | Keyboard input                   |
| `EntityManager.java`    | Entity update/render loop        |
| `CollisionManager.java` | Collision detection              |
| `SpriteLoader.java`     | Image loading                    |
| `Animator.java`         | Sprite animation                 |
| `Assets.java`           | Sprite storage                   |
| `Constants.java`        | Screen size, FPS, tile size      |
| `resources/`            | Sprites + sounds                 |

Directory Structure
resources/
├── sprites/
│   ├── player.png        (48×48 or 48×192 spritesheet: 4 directions)
│   ├── enemy.png         (48×48)
│   ├── boss.png          (64×64 or 96×96)
│   ├── chest.png         (48×48) said
│   ├── chest_open.png    (48×48) said
│   ├── barrel.png        (48×48) said
│   ├── barrel_broken.png (48×48) said
│   ├── door_locked.png   (48×48)
│   ├── door_open.png     (48×48)
│   ├── key.png           (32×32) said
│   ├── apple.png         (32×32) said
│   ├── cake.png          (32×32) said
│   ├── potion.png        (32×32) said
│   ├── crowbar.png       (32×32) said
│   ├── gun.png           (32×32) said
│   ├── bullet.png        (16×16) said
│   ├── floor.png         (48×48)
│   ├── wall.png          (48×48)
│   └── achievement.png   (48×48)
└── sounds/               (optional)
    ├── attack.wav
    ├── pickup.wav
    ├── door_open.wav
    └── game_over.wav
