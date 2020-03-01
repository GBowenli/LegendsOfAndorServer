package LoAServer.Die;

import LoAServer.LoAServer;

import java.util.ArrayList;
import java.util.Random;

public class Die {
    ArrayList<Integer> dieFaces;

    public Die() {}

    public Die(ArrayList<Integer> dieFaces) {
        this.dieFaces = dieFaces;
    }

    public Integer rollDie() {
        Random random = new Random();
        int rand = random.nextInt(dieFaces.size());

        return dieFaces.get(rand);
    }

    public Integer flipDie(Integer indexOfRoll) {
        if (indexOfRoll == 0) {
            return dieFaces.get(5);
        } else if (indexOfRoll == 1) {
            return dieFaces.get(4);
        } else if (indexOfRoll == 2) {
            return dieFaces.get(3);
        } else if (indexOfRoll == 3) {
            return dieFaces.get(2);
        } else if (indexOfRoll == 4) {
            return dieFaces.get(1);
        } else { // == 5
            return dieFaces.get(0);
        }
    }

    public ArrayList<Integer> getDieFaces() {
        return dieFaces;
    }

    public void setDieFaces(ArrayList<Integer> dieFaces) {
        this.dieFaces = dieFaces;
    }
}
