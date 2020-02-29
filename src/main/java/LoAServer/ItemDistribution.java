package LoAServer;

import LoAServer.Item.Item;

import java.util.ArrayList;

public class ItemDistribution {
    private int warriorGold;
    private int archerGold;
    private int wizardGold;
    private int dwarfGold;
    private ArrayList<Item> warriorItems;
    private ArrayList<Item> wizardItems;
    private ArrayList<Item> archerItems;
    private ArrayList<Item> dwarfItems;

    public ItemDistribution() {
        warriorItems = new ArrayList<>();
        wizardItems = new ArrayList<>();
        archerItems = new ArrayList<>();
        dwarfItems = new ArrayList<>();
    }
    public int getWarriorGold() {
        return warriorGold;
    }

    public void setWarriorGold(int warriorGold) {
        this.warriorGold = warriorGold;
    }

    public int getArcherGold() {
        return archerGold;
    }

    public void setArcherGold(int archerGold) {
        this.archerGold = archerGold;
    }

    public int getWizardGold() {
        return wizardGold;
    }

    public void setWizardGold(int wizardGold) {
        this.wizardGold = wizardGold;
    }

    public int getDwarfGold() {
        return dwarfGold;
    }

    public void setDwarfGold(int dwarfGold) {
        this.dwarfGold = dwarfGold;
    }

    public ArrayList<Item> getWarriorItems() {
        return warriorItems;
    }

    public void setWarriorItems(ArrayList<Item> warriorItems) {
        this.warriorItems = warriorItems;
    }

    public ArrayList<Item> getWizardItems() {
        return wizardItems;
    }

    public void setWizardItems(ArrayList<Item> wizardItems) {
        this.wizardItems = wizardItems;
    }

    public ArrayList<Item> getArcherItems() {
        return archerItems;
    }

    public void setArcherItems(ArrayList<Item> archerItems) {
        this.archerItems = archerItems;
    }

    public ArrayList<Item> getDwarfItems() {
        return dwarfItems;
    }

    public void setDwarfItems(ArrayList<Item> dwarfItems) {
        this.dwarfItems = dwarfItems;
    }


}