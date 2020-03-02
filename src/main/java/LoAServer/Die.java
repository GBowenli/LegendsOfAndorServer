package LoAServer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

enum DieType {
    REGULAR_DIE, BLACK_DIE
}

public class Die {
    ArrayList<Integer> dieFaces;

    public Die() {}

    public Die(DieType dieType) {
        if (dieType == DieType.REGULAR_DIE) {
            dieFaces = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6));
        } else { // black die
            dieFaces = new ArrayList<Integer>(Arrays.asList(8, 9, 9, 10, 10, 12));
        }
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
}
