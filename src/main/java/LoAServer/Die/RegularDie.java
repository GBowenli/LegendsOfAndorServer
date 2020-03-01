package LoAServer.Die;

import java.util.ArrayList;
import java.util.Arrays;

public class RegularDie extends Die {
    public RegularDie() {
        super(new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6)));
    }
}
