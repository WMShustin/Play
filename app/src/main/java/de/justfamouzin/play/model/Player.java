package de.justfamouzin.play.model;

/**
 * @author Justin@Famouz
 */

public class Player {

    private String name;
    private int points;
    private TeamBet teamBet;

    public Player(){}

    public Player(String name) {
        this.name = name;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public TeamBet getTeamBet() {
        return teamBet;
    }

    public int getPoints() {
        return points;
    }

    public void setTeamBet(TeamBet teamBet) {
        this.teamBet = teamBet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name + " | " + points;
    }
}
