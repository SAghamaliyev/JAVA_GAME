package items;

public abstract class Food extends Item {
    protected int healAmount;  
    protected boolean speedBoost;    

    public Food(String name, int healAmount, boolean speedBoost) {
        super(name);
        this.healAmount = healAmount;
        this.speedBoost = speedBoost;

        
        this.consumable = true;
    }

    
    public int getHealAmount() {
        return healAmount;
    }

    public boolean getSpeedBoost() {
        return speedBoost;
    }
}
