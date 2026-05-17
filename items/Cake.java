package items;

import graphics.Assets;

public class Cake extends Food {
  
  public Cake(){
    super("Cake", 25, false);
    this.icon = Assets.cakeIcon;
  }

}
