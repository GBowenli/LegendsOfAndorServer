package LoAServer;

enum ItemType {
    WINESKIN,FALCON,BOW,HELM,SHIELD,TELESCOPE, WITCH_BREW, MEDICINAL_HERB
}

public class Item {
    private ItemType itemType;
    private int numUses;

    public Item() {
    }

    public Item(ItemType itemType) {
        this.itemType = itemType;
        if (itemType == ItemType.WINESKIN) {
            this.numUses = 2;
            //falcon can only be used once per day
        } else if (itemType == ItemType.FALCON) {
            this.numUses = 1;
        } else if (itemType == ItemType.SHIELD){
            this.numUses = 2;
        } else if (itemType == ItemType.WITCH_BREW) {
            this.numUses = 2;
        }else{
            this.numUses = 0;
        }
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public int getNumUses() {
        return this.numUses;
    }

    public void setNumUses(int numUses) {
        this.numUses = numUses;
    }


    public boolean isSmallItem(){
        if(this.itemType == ItemType.MEDICINAL_HERB || this.itemType == ItemType.WINESKIN || this.itemType == ItemType.WITCH_BREW
                || this.itemType == ItemType.TELESCOPE){
            return true;
        }else{
            return false;
        }
    }

    public boolean isLargeItem(){
        if(this.itemType == ItemType.FALCON || this.itemType == ItemType.SHIELD || this.itemType == ItemType.BOW){
            return true;
        }else{
            return false;
        }
    }

    public boolean isHelm(){
        return this.itemType == ItemType.HELM;
    }
}