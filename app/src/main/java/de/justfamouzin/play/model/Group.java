package de.justfamouzin.play.model;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Justin@Famouz
 */

public class Group {

    private String name;
    private Team winner;
    private Team runnerup;
    private List<Game> matches = Lists.newArrayList();
    private List<Team> teams = Lists.newArrayList();
    private boolean ko;

    public Group(JsonObject jsonObject) {
        this(jsonObject, false);
    }

    public Group(JsonObject jsonObject, boolean ko) {
        this.name = jsonObject.get("name").getAsString();
        matches = initMatches(jsonObject);
        this.ko = ko;
    }

    public List<Game> initMatches(JsonObject jsonObject) {
        List<Game> list = Lists.newArrayList();
        JsonArray jsonArray = jsonObject.get("matches").getAsJsonArray();
        for(int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject1 = jsonArray.get(i).getAsJsonObject();
            Game game = new Game(jsonObject1);
            Team home = game.getHome();
            Team away = game.getHome();
            if (!teams.contains(home)) teams.add(home);
            if (!teams.contains(away)) teams.add(away);
            if(!this.ko) addGroupStats(game);
            list.add(game);
        }
        return list;
    }

    private void addGroupStats(Game game) {
        Team home  = game.getHome();
        Team away = game.getAway();
        if(!teams.contains(home)) teams.add(home);
        if(!teams.contains(away)) teams.add(away);
        if(game.isFinished()) {
            int home_goals = game.getHome_result();
            int away_goals = game.getAway_result();
            home.addGoals(home_goals);
            home.addAgainst(away_goals);
            away.addGoals(away_goals);
            away.addAgainst(home_goals);
            if(home_goals > away_goals) {
                home.addWins(1);
                away.addLoses(1);
            } else if(away_goals > home_goals) {
                home.addLoses(1);
                away.addWins(1);
            } else {
                home.addEquals(1);
                away.addEquals(1);
            }
        }
        sortGroup();
    }

    private void sortGroup() {
        Collections.sort(teams, new Comparator<Team>() {
            @Override
            public int compare(Team one, Team two) {
                if(one.getBetterTeam(two).getName().equals(two.getName()))
                    return 1;
                else
                    return -1;
            }
        });
    }

    public Game getGame(int id) {
        for(Game game : matches) {
            if(game.getName() == id) return game;
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public List<Game> getMatches() {
        return matches;
    }

    public Team getRunnerup() {
        return runnerup;
    }

    public Team getWinner() {
        return winner;
    }

    public List<Team> getTeams() {
        return teams;
    }

    @Override
    public String toString() {
        return super.toString() + " : "  + matches.toString();
    }
}
