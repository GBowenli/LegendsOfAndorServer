package LoAServer;

enum ItemType {
    WINESKIN,FALCON,BOW,HELM,SHIELD,TELESCOPE
}

public class Item {
    int uses;

    public Item() {
        this.uses = 0;
    }

    public Item(ItemType itemType) {
        if (itemType == ItemType.WINESKIN) {
            uses = 2;
            //falcon can only be used once per day
        } else if (itemType == ItemType.FALCON) {
            uses = 1;
        } else if (itemType == ItemType.SHIELD){
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



