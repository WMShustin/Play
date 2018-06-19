package de.justfamouzin.play.model;

/**
 * @author Justin@Famouz
 */

public class GameBet extends Bet {

    private int id;
    private int home;
    private int away;

    public GameBet() {}

    public GameBet(int id, int home, int away) {
        this.id = id;
        this.home = home;
        this.away = away;
    }

    public int getId() {
        return id;
    }

    public int getAway() {
        return away;
    }

    public int getHome() {
        return home;
    }

    public void setAway(int away) {
        this.away = away;
    }

    public void setHome(int home) {
        this.home = home;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id + " : " + home + ":" + away;
    }
}
