package LoAServer;

public class Farmer {
    boolean isBeingCarried;

    public Farmer() {}

    public Farmer(boolean isBeingCarried) {
        this.isBeingCarried = isBeingCarried;
    }

    public boolean isBeingCarried() {
        return isBeingCarried;
    }

    public void setBeingCarried(boolean beingCarried) {
        isBeingCarried = beingCarried;
    }
}
