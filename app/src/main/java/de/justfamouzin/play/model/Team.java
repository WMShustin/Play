package de.justfamouzin.play.model;

import com.google.gson.JsonObject;

/**
 * @author Justin@Famouz
 */

public class Team {

    private int id;
    private String name;
    private String shortName;
    private String emoji;
    private int wins;
    private int loses;
    private int equals;
    private int goals;
    private int against;
    private boolean active;

    public Team(JsonObject jsonObject) {
        this.id = jsonObject.get("id").getAsInt();
        this.name = jsonObject.get("name").getAsString();
        this.shortName = jsonObject.get("fifaCode").getAsString();
        this.emoji = jsonObject.get("emojiString").getAsString();
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getEmoji() {
        return emoji;
    }

    public String getShortName() {
        return shortName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getAgainst() {
        return against;
    }

    public int getEquals() {
        return equals;
    }

    public int getGoals() {
        return goals;
    }

    public int getLoses() {
        return loses;
    }

    public int getWins() {
        return wins;
    }

    public int getPoints() {
        return wins*3 + equals;
    }

    public int getPlayed() {
        return wins+loses+equals;
    }

    public void addWins(int wins) {
        this.wins+=wins;
    }

    public void addLoses(int loses) {
        this.loses+=loses;
    }

    public void addEquals(int equals) {
        this.equals+=equals;
    }

    public void addGoals(int goals) {
        this.goals+=goals;
    }

    public void addAgainst(int against) {
        this.against+=against;
    }

    public Team getBetterTeam(Team two) {
        if(getPoints() > two.getPoints()) return this;
        if(getPoints() < two.getPoints()) return two;
        if(getGoals()-getAgainst() > two.getGoals()-two.getAgainst()) return this;
        if(getGoals()-getAgainst() < two.getGoals()-two.getAgainst()) return two;
        if(getGoals()-getAgainst() == two.getGoals()-two.getAgainst()) {
            if(getGoals() > two.getGoals()) return this; else return two;
        }
        return this;
    }

    @Override
    public String toString() {
        return name;
    }
}

