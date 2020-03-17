package LoAServer;

enum Colour {
    GREEN, BLUE, YELLOW
}

public class RuneStone {
    private Colour colour;

    public RuneStone() {}

    public RuneStone(Colour colour) {
        this.colour = colour;
    }

    public Colour getColour() {
        return colour;
    }

    public void setColour(Colour colour) {
        this.colour = colour;
    }
}
