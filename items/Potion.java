package items;

import graphics.Assets;

public class Potion extends Food {
  
  public Potion(){
    super("Potion", 50, true);
    this.icon = Assets.potionIcon;
  }

}
