package LoAServer;

enum Colour {
    GREEN, BLUE, RED
}

public class GemStone {
    private Colour colour;
    private int value;

    public GemStone() {}

    public GemStone(Colour colour, int value) {
        this.colour = colour;
        this.value = value;
    }

    public Colour getColour() {
        return colour;
    }

    public void setColour(Colour colour) {
        this.colour = colour;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
