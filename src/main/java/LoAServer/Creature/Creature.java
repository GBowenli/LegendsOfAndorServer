package LoAServer.Creature;

public class Creature {
    private int strength;
    private int willpower;
    private int goldReward;
    private int willpowerReward;

    public Creature() {}

    public Creature(int strength, int willpower, int goldReward, int willpowerReward) {
        this.strength = strength;
        this.willpower = willpower;
        this.goldReward = goldReward;
        this.willpowerReward = willpowerReward;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getWillpower() {
        return willpower;
    }

    public void setWillpower(int willpower) {
        this.willpower = willpower;
    }

    public int getGoldReward() {
        return goldReward;
    }

    public void setGoldReward(int goldReward) {
        this.goldReward = goldReward;
    }

    public int getWillpowerReward() {
        return willpowerReward;
    }

    public void setWillpowerReward(int willpowerReward) {
        this.willpowerReward = willpowerReward;
    }
}
