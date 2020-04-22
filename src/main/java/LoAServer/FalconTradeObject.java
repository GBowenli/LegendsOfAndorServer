package LoAServer;

public class FalconTradeObject {
    private HeroClass p1_heroclass;
    private HeroClass p2_heroclass;
    private int p1_gold;
    private int p1_runestone_blue;
    private int p1_wineskin;
    private int p1_runestone_green;
    private int p1_telescope;
    private int p1_helm;
    private int p1_medicinal_herb;
    private int p1_witch_brew;
    private int p1_runestone_yellow;
    private int p2_gold;
    private int p2_runestone_blue;
    private int p2_wineskin;
    private int p2_runestone_green;
    private int p2_telescope;
    private int p2_helm;
    private int p2_medicinal_herb;
    private int p2_witch_brew;
    private int p2_runestone_yellow;
    private boolean p1_hasConfirmed;
    private boolean p2_hasConfirmed;
    private boolean dontUpdate;


    public int getP1_gold() {
        return p1_gold;
    }

    public void setP1_gold(int p1_gold) {
        this.p1_gold = p1_gold;
    }

    public int getP1_runestone_blue() {
        return p1_runestone_blue;
    }

    public void setP1_runestone_blue(int p1_runestone_blue) {
        this.p1_runestone_blue = p1_runestone_blue;
    }

    public int getP1_wineskin() {
        return p1_wineskin;
    }

    public void setP1_wineskin(int p1_wineskin) {
        this.p1_wineskin = p1_wineskin;
    }

    public int getP1_runestone_green() {
        return p1_runestone_green;
    }

    public void setP1_runestone_green(int p1_runestone_green) {
        this.p1_runestone_green = p1_runestone_green;
    }

    public int getP1_telescope() {
        return p1_telescope;
    }

    public void setP1_telescope(int p1_telescope) {
        this.p1_telescope = p1_telescope;
    }

    public int getP1_helm() {
        return p1_helm;
    }

    public void setP1_helm(int p1_helm) {
        this.p1_helm = p1_helm;
    }

    public int getP1_medicinal_herb() {
        return p1_medicinal_herb;
    }

    public void setP1_medicinal_herb(int p1_medicinal_herb) {
        this.p1_medicinal_herb = p1_medicinal_herb;
    }

    public int getP1_witch_brew() {
        return p1_witch_brew;
    }

    public void setP1_witch_brew(int p1_witch_brew) {
        this.p1_witch_brew = p1_witch_brew;
    }

    public int getP1_runestone_yellow() {
        return p1_runestone_yellow;
    }

    public void setP1_runestone_yellow(int p1_runestone_yellow) {
        this.p1_runestone_yellow = p1_runestone_yellow;
    }

    public int getP2_gold() {
        return p2_gold;
    }

    public void setP2_gold(int p2_gold) {
        this.p2_gold = p2_gold;
    }

    public int getP2_runestone_blue() {
        return p2_runestone_blue;
    }

    public void setP2_runestone_blue(int p2_runestone_blue) {
        this.p2_runestone_blue = p2_runestone_blue;
    }

    public int getP2_wineskin() {
        return p2_wineskin;
    }

    public void setP2_wineskin(int p2_wineskin) {
        this.p2_wineskin = p2_wineskin;
    }

    public int getP2_runestone_green() {
        return p2_runestone_green;
    }

    public void setP2_runestone_green(int p2_runestone_green) {
        this.p2_runestone_green = p2_runestone_green;
    }

    public int getP2_telescope() {
        return p2_telescope;
    }

    public void setP2_telescope(int p2_telescope) {
        this.p2_telescope = p2_telescope;
    }

    public int getP2_helm() {
        return p2_helm;
    }

    public void setP2_helm(int p2_helm) {
        this.p2_helm = p2_helm;
    }

    public int getP2_medicinal_herb() {
        return p2_medicinal_herb;
    }

    public void setP2_medicinal_herb(int p2_medicinal_herb) {
        this.p2_medicinal_herb = p2_medicinal_herb;
    }

    public int getP2_witch_brew() {
        return p2_witch_brew;
    }

    public void setP2_witch_brew(int p2_witch_brew) {
        this.p2_witch_brew = p2_witch_brew;
    }

    public int getP2_runestone_yellow() {
        return p2_runestone_yellow;
    }

    public void setP2_runestone_yellow(int p2_runestone_yellow) {
        this.p2_runestone_yellow = p2_runestone_yellow;
    }

    public HeroClass getP1_heroclass() {
        return p1_heroclass;
    }

    public void setP1_heroclass(HeroClass p1_heroclass) {
        this.p1_heroclass = p1_heroclass;
    }

    public HeroClass getP2_heroclass() {
        return p2_heroclass;
    }

    public void setP2_heroclass(HeroClass p2_heroclass) {
        this.p2_heroclass = p2_heroclass;
    }

    public boolean isP1_hasConfirmed() {
        return p1_hasConfirmed;
    }

    public void setP1_hasConfirmed(boolean p1_hasConfirmed) {
        this.p1_hasConfirmed = p1_hasConfirmed;
    }

    public boolean isP2_hasConfirmed() {
        return p2_hasConfirmed;
    }

    public void setP2_hasConfirmed(boolean p2_hasConfirmed) {
        this.p2_hasConfirmed = p2_hasConfirmed;
    }

    public boolean isDontUpdate() {
        return dontUpdate;
    }

    public void setDontUpdate(boolean dontUpdate) {
        this.dontUpdate = dontUpdate;
    }

    public String toString(){
        return "Values: " +
                "p1 gold: " + this.p1_gold +
                "p1 wineskin: " + this.p1_wineskin +
                "p1 blue runestone: " + this.p1_runestone_blue +
                "p1 yellow runestone: " + this.p1_runestone_yellow +
                "p1 telescope: " + this.p1_telescope +
                "p1 helm: " + this.p1_helm +
                "p1 medicinal herb: " + this.p1_medicinal_herb +
                "p1 witch brew: " + this.p1_witch_brew +
                "p1 runestone green: " + this.p1_runestone_green +
                "p2 gold: " + this.p2_gold +
                "p2 wineskin: " + this.p2_wineskin +
                "p2 blue runestone: " + this.p2_runestone_blue +
                "p2 yellow runestone: " + this.p2_runestone_yellow +
                "p2 telescope: " + this.p2_telescope +
                "p2 helm: " + this.p2_helm +
                "p2 medicinal herb: " + this.p2_medicinal_herb +
                "p2 witch brew: " + this.p2_witch_brew +
                "p2 runestone green: " + this.p2_runestone_green;
    }
}