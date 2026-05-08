package items;

public class Apple extends Food {
  
  public Apple(){
    super("Apple", 5, false);
  }

  @Override
  public void use(){
    // Sound/Animation of eating
  }
}
