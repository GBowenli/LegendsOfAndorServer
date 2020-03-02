package LoAServer;

enum ItemType {
    WINESKIN
}

public class Item {
    int uses;

    public Item(){this.uses = 0;}

    public Item (ItemType itemType){
        if (itemType == ItemType.WINESKIN) {
            uses = 2;
        }
    }
    public int getNumUses() {
        return uses;
    }

    public void setNumUses(int uses) {
        this.uses = uses;
    }



}