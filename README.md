# 🎮 Java Dungeon Game

Welcome to **Java Dungeon Game**, a 2D tile-based dungeon crawler! Your goal is simple: fight your way through five interconnected rooms, gather loot, and defeat the boss to escape to freedom. 

We built this game from scratch using Java Swing. No massive game engines, just pure Java!

---

## 🗡️ What's Inside?

- **Explore the Dungeon:** Navigate through 5 unique rooms filled with enemies, chests, and locked doors.
- **Combat & Weapons:** Start with your bare fists (melee), then hunt down the **Gun** to blast enemies from afar!
- **Inventory & Loot:** Collect items like apples and potions to heal up. Find the **Crowbar** to smash open chests, and collect **Keys** to unlock the path forward. You can equip the gun directly from your inventory!
- **Smart Checkpoints:** Activate golden stars in each room to set your respawn point. If you fall in battle, you'll respawn right back at the last checkpoint you touched!
- **Procedural Graphics:** Don't have sprites? No problem! The game runs perfectly using colorful procedural shapes. But if you want to spice things up, you can drop your own `.png` sprites right into the `resources` folder!

---

## 🚀 How to Play

### Getting Started

You'll need **Java 17** or higher. You can run this in any IDE (like IntelliJ or Eclipse), or just use the command line:

**1. Compile the code:**
```bash
javac -d out entities/*.java items/*.java managers/*.java world/*.java graphics/*.java input/*.java main/*.java ui/*.java utils/*.java
```

**2. Run the game:**
```bash
cp main/bella_ciao.wav out/main/bella_ciao.wav
java -cp out main.Main
```

### Controls

| Key | Action |
|-----|--------|
| `W` `A` `S` `D` / Arrows | Move around |
| `Space` | Attack (Melee, or Fire if Gun is equipped) |
| `E` | Interact (Smash chests/barrels, unlock doors) |
| `I` | Open/Close Inventory |
| `1`-`9` | Use or Equip an item from your inventory |
| `Esc` | Pause the game |

---

## 🗺️ Your Journey

Here's a quick guide to what you'll face:

1. **Room 1 (Entrance Hall):** Take out the first enemy to get the **Crowbar**. Use the crowbar on the chest to get **Key 1**, and unlock the door!
2. **Room 2 (The Corridor):** Battle through the guards, grab the next key, and keep moving.
3. **Room 3 (The Armory):** This is it—find the **Gun** hidden in the chest! Smash the barrels for some extra health.
4. **Room 4 (The Gauntlet):** A maze filled with enemies. You'll need to be quick on your feet and generous with your potions.
5. **Room 5 (Boss Chamber):** The final showdown. Defeat the massive Boss and his minions to win the game!

