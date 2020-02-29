package LoAServer;

import LoAServer.Creature.Creature;

import java.util.ArrayList;

public class Fight {
    private ArrayList<Hero> heroes;
    private ArrayList<Hero> pendingInvitedHeroes;
    private Creature creature;

    public Fight() {}

    public Fight(Hero hero, Creature creature) {
        heroes = new ArrayList<>();
        heroes.add(hero);
        pendingInvitedHeroes = new ArrayList<>();
        this.creature = creature;
    }

    public boolean canStartFight() {
        if (pendingInvitedHeroes.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<Hero> getHeroes() {
        return heroes;
    }

    public void setHeroes(ArrayList<Hero> heroes) {
        this.heroes = heroes;
    }

    public ArrayList<Hero> getPendingInvitedHeroes() {
        return pendingInvitedHeroes;
    }

    public void setPendingInvitedHeroes(ArrayList<Hero> pendingInvitedHeroes) {
        this.pendingInvitedHeroes = pendingInvitedHeroes;
    }

    public Creature getCreature() {
        return creature;
    }

    public void setCreature(Creature creature) {
        this.creature = creature;
    }
}
