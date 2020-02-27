package LoAServer;

import LoAServer.Item.Item;

import java.util.ArrayList;

public class ItemDistribution {
    private int player1Gold;
    private int player2Gold;
    private int player3Gold;
    private int player4Gold;
    private ArrayList<Item> player1Items;
    private ArrayList<Item> player2Items;
    private ArrayList<Item> player3Items;
    private ArrayList<Item> player4Items;

    public ItemDistribution() {
        player1Items = new ArrayList<>();
        player2Items = new ArrayList<>();
        player3Items = new ArrayList<>();
        player4Items = new ArrayList<>();
    }

    public int getPlayer1Gold() {
        return player1Gold;
    }

    public void setPlayer1Gold(int player1Gold) {
        this.player1Gold = player1Gold;
    }

    public int getPlayer2Gold() {
        return player2Gold;
    }

    public void setPlayer2Gold(int player2Gold) {
        this.player2Gold = player2Gold;
    }

    public int getPlayer3Gold() {
        return player3Gold;
    }

    public void setPlayer3Gold(int player3Gold) {
        this.player3Gold = player3Gold;
    }

    public int getPlayer4Gold() {
        return player4Gold;
    }

    public void setPlayer4Gold(int player4Gold) {
        this.player4Gold = player4Gold;
    }

    public ArrayList<Item> getPlayer1Items() {
        return player1Items;
    }

    public void setPlayer1Items(ArrayList<Item> player1Items) {
        this.player1Items = player1Items;
    }

    public ArrayList<Item> getPlayer2Items() {
        return player2Items;
    }

    public void setPlayer2Items(ArrayList<Item> player2Items) {
        this.player2Items = player2Items;
    }

    public ArrayList<Item> getPlayer3Items() {
        return player3Items;
    }

    public void setPlayer3Items(ArrayList<Item> player3Items) {
        this.player3Items = player3Items;
    }

    public ArrayList<Item> getPlayer4Items() {
        return player4Items;
    }

    public void setPlayer4Items(ArrayList<Item> player4Items) {
        this.player4Items = player4Items;
    }
}
