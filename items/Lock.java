package items;

public class Lock {

    private boolean locked;
    private String  requiredKeyId;

    
    public Lock(String requiredKeyId) {
        this.locked  = true;
        this.requiredKeyId = requiredKeyId;
    }

    
    public Lock() {
        this.locked  = false;
        this.requiredKeyId = null;
    }

    
    public boolean unlock(String keyId) {
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

    public String getRequiredKeyId() { 
        return requiredKeyId; 
    }
}
