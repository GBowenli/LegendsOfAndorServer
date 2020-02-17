package LoAServer;


public class Wineskin extends Item {
    int uses;

    public Wineskin(){
        this.uses = 2;
    }

    public int getUses() {
        return uses;
    }

    public void setUses(int uses) {
        this.uses = uses;
    }
}

