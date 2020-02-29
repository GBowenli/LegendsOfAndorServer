package LoAServer.Item;

public class Item {
    int uses;

    public Item(){this.uses = 0;}

    public Item (int numUses){
        this.uses = numUses;
    }
    public int getNumUses() {
        return uses;
    }

    public void setNumUses(int uses) {
        this.uses = uses;
    }



}