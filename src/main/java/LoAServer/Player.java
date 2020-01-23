package LoAServer;

public class Player {
    private String username;
    private String password;
    private String color;

    public Player() {
    }

    public Player(String username, String password, String color) {
        this.username = username;
        this.password = password;
        this.color = color;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }
}
