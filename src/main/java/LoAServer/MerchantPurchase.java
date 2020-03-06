package LoAServer;

import java.util.ArrayList;

public class MerchantPurchase {
    private int strength;
    private ArrayList<Item> items;

    public MerchantPurchase() {}

    public MerchantPurchase(int strength, ArrayList<Item> items) {
        this.strength = strength;
        this.items = items;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }
}
