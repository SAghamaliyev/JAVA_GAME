package items;

public class Lock {

    private boolean locked;
    private int  requiredKeyId;

    
    public Lock(int requiredKeyId) {
        this.locked  = true;
        this.requiredKeyId = requiredKeyId;
    }

    
    public Lock() {
        this.locked  = false;
        this.requiredKeyId = null;
    }

    
    public boolean unlock(int keyId) {
        if (locked && requiredKeyId != null && requiredKeyId.equals(keyId)) {
            locked = false;
            return true;
        }
        return false;
    }

    public void lock() { 
        this.locked = true; 
    }

    public boolean isLocked() { 
        return locked; 
    }

    public int getRequiredKeyId() { 
        return requiredKeyId; 
    }
}
