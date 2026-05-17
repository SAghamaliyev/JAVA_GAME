package items;

import graphics.Assets;

public class Apple extends Food {
  
  public Apple(){
    super("Apple", 5, false);
    this.icon = Assets.appleIcon;
  }

}
