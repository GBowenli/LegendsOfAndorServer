package LoAServer;

import java.util.ArrayList;

enum HeroClass {
    WARRIOR,ARCHER,DWARF,WIZARD
}

public class Hero {
    private HeroClass heroClass;
    private int willPower;
    private int strength;
    private int currentSpace;
    private int currentHour;
    private boolean hasEndedDay;
    private int gold;
    private ArrayList<Item> items;
    private ArrayList<Farmer> farmers;
    private boolean moved;
    private boolean fought;

    public Hero() {}

    public Hero(HeroClass heroClass) {
        this.heroClass = heroClass;

        if (heroClass == HeroClass.ARCHER) {
            currentSpace = 53;
        } else if (heroClass == HeroClass.DWARF) {
            currentSpace = 43;
        } else if (heroClass == HeroClass.WARRIOR) {
            currentSpace = 25;
        } else { // wizard
            currentSpace = 9;
        }
        willPower = 7;
        strength = 1;
        currentHour = 0;
        hasEndedDay = false;
        gold = 0;
        items = new ArrayList<>();
        farmers = new ArrayList<>();
    }
    public HeroClass getHeroClass() {
        return heroClass;
    }

    public void setHeroClass(HeroClass heroClass) {
        this.heroClass = heroClass;
    }

    public int getWillPower() {
        return willPower;
    }

    public void setWillPower(int willPower) {
        this.willPower = willPower;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getCurrentSpace() {
        return currentSpace;
    }

    public void setCurrentSpace(int currentSpace) {
        this.currentSpace = currentSpace;
    }

    public int getCurrentHour() {
        return currentHour;
    }

    public void setCurrentHour(int currentHour) {
        this.currentHour = currentHour;
    }

    public boolean isHasEndedDay() {
        return hasEndedDay;
    }

    public void setHasEndedDay(boolean hasEndedDay) {
        this.hasEndedDay = hasEndedDay;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public ArrayList<Farmer> getFarmers() {
        return farmers;
    }

    public void setFarmers(ArrayList<Farmer> farmers) {
        this.farmers = farmers;
    }

    public boolean isMoved() {
        return moved;
    }

    public void setMoved(boolean moved) {
        this.moved = moved;
    }


    public boolean isFought() {
        return fought;
    }

    public void setFought(boolean fought) {
        this.fought = fought;
    }
}
