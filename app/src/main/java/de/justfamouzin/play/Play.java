package de.justfamouzin.play;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.common.collect.Lists;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.justfamouzin.play.model.Game;
import de.justfamouzin.play.model.GameBet;
import de.justfamouzin.play.model.Group;
import de.justfamouzin.play.model.Player;
import de.justfamouzin.play.model.Stadium;
import de.justfamouzin.play.model.Team;
import de.justfamouzin.play.model.TeamBet;
import de.justfamouzin.play.util.Util;

/**
 * @author Justin@Famouz
 */

public class Play {

    private SQLiteDatabase database;
    private FirebaseUser firebaseUser;
    private RequestQueue requestQueue;

    private static Play instance;
    private Util util;

    public static Gson gson;
    private JsonElement jsonElement;

    public ArrayList<Stadium> stadiumList;
    public List<Team> teamList;
    public List<Group> groupList;
    public List<GameBet> bets;
    public List<Game> matches;
    public List<Player> playerList;

    public int points;

    public TeamBet teamBet;

    public Play(Context context) {
        instance = this;
        stadiumList = Lists.newArrayList();
        teamList = Lists.newArrayList();
        groupList = Lists.newArrayList();
        bets = Lists.newArrayList();
        matches = Lists.newArrayList();
        playerList = Lists.newArrayList();

        this.util = Util.getInstance(context);
        this.requestQueue = util.getRequestQueue();

        /*
        // Create a DB helper (this will create the DB if run for the first time)
        GameStatsDbHelper dbHelper = new GameStatsDbHelper(this);

        // Keep a reference to the mDb until paused or killed. Get a writable database
        // because you will be adding restaurant customers
        database = dbHelper.getWritableDatabase();
        */
    }


    public void loadData() {
        String url = Util.getUrl();
        Log.d("Reading content: ", url);
        StringRequest request = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        setJsonObject(new JsonParser().parse(response));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Resopnce", String.valueOf(error));
                    }
                });
        request.setTag("WM18");
        requestQueue.add(request);
    }

    public void initData() {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        stadiumList.addAll(util.initStadiums(jsonObject));
        teamList.addAll(util.initTeams(jsonObject));
        groupList.addAll(util.initGroups(jsonObject));
        if(util.groupsFinished()) groupList.addAll(util.initKnockouts(jsonObject));
        matches.addAll(getAllMatches());
    }

    public void setJsonObject(JsonElement jsonElement) {
        this.jsonElement = jsonElement;
        initData();
    }

    public List<Game> getAllMatches() {
        List<Game> matches = Lists.newArrayList();
        for(Group group : groupList) {
            matches.addAll(group.getMatches());
        }
        return matches;
    }

    public Game getMatch(int id) {
        for(Game game : matches) {
            if(game.getName() == id) {
                return game;
            }
        }
        return null;
    }

    public List<Player> getPlayerList() {
        Collections.sort(playerList, new Comparator<Player>() {
            @Override
            public int compare(Player one, Player two) {
                if(one.getPoints() > two.getPoints())
                    return -1;
                else
                    return 1;
            }
        });
        Log.d("XXX", "XXX");
        return playerList;
    }

    public void addPlayer(Player player) {
        playerList.add(player);
    }

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }

    public Stadium getStadium(int id) {
        for(Stadium s : stadiumList) {
            if(s.getId() == id) {
                return s;
            }
        }
        return null;
    }

    public Team getTeam(int id) {
        Team team = null;
        for(Team s : teamList) {
            if(s.getId() == id) team = s;
        }
        return team;
    }

    public List<Team> getTeamList() {
        return teamList;
    }

    public ArrayList<Stadium> getStadiumList() {
        return this.stadiumList;
    }

    public List<Group> getGroupList() {
        return groupList;
    }

    public Util getUtil() {
        return util;
    }

    public void addBet(GameBet gameBet) {
        this.bets.add(gameBet);
    }

    public List<GameBet> getBets() {
        return bets;
    }

    public GameBet getBet(int id) {
        for(GameBet s : bets) {
            if(s.getId() == id) {
                return s;
            }
        }
        return null;
    }

    public void setTeamBet(TeamBet teamBet) {
        this.teamBet = teamBet;
    }

    public TeamBet getTeamBet() {
        return teamBet;
    }

    public static Play getInstance() {
        return instance;
    }

    public FirebaseUser getFirebaseUser() {
        return firebaseUser;
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public void setFirebaseUser(FirebaseUser firebaseUser) {
        this.firebaseUser = firebaseUser;
        util.setUsername();
    }

    @Override
    public String toString() {
        return stadiumList.toString();
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getPoints() {
        return points;
    }
}
