package LoAServer;

enum ItemType {
    WINESKIN,FALCON,BOW,HELM,SHIELD,TELESCOPE, WITCH_BREW, MEDICINAL_HERB
}

public class Item {
    private ItemType itemType;
    private int uses;

    public Item() {
    }

    public Item(ItemType itemType) {
        this.itemType = itemType;
        if (itemType == ItemType.WINESKIN) {
            this.uses = 2;
            //falcon can only be used once per day
        } else if (itemType == ItemType.FALCON) {
            this.uses = 0;
        } else if (itemType == ItemType.SHIELD){
            this.uses = 2;
        } else if (itemType == ItemType.WITCH_BREW) {
            this.uses = 2;
        }else{
            this.uses = 0;
        }
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public int getNumUses() {
        return uses;
    }

    public void setNumUses(int uses) {
        this.uses = uses;
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
        if(this.itemType == ItemType.FALCON || this.itemType == ItemType.SHIELD || this.itemType == ItemType.BOW) {
            return true;
        }else{
            return false;
        }
    }

    public boolean isHelm(){
        return this.itemType == ItemType.HELM;
    }
}