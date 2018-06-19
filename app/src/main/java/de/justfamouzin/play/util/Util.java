package de.justfamouzin.play.util;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.common.collect.Lists;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Calendar;
import java.util.List;

import de.justfamouzin.play.Play;
import de.justfamouzin.play.model.Game;
import de.justfamouzin.play.model.GameBet;
import de.justfamouzin.play.model.Group;
import de.justfamouzin.play.model.Stadium;
import de.justfamouzin.play.model.Team;
import de.justfamouzin.play.model.TeamBet;

/**
 * @author Justin@Famouz
 */

public class Util {

    private static Util mInstance;
    private RequestQueue mRequestQueue;
    private Context mCtx;
    private static final String url = "https://raw.githubusercontent.com/lsv/fifa-worldcup-2018/master/data.json";

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("bets");


    String[] days = new String[]{"So", "Mo", "Di", "Mi", "Do", "Fr", "Sa"};

    public Util(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    public static String getUrl() {
        return url;
    }

    public static synchronized Util getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new Util(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public List<Team> initTeams(JsonObject jsonObject) {
        List<Team> list = Lists.newArrayList();
        JsonArray jsonArray = jsonObject.get("teams").getAsJsonArray();
        for(int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject1 = jsonArray.get(i).getAsJsonObject();
            Team team = new Team(jsonObject1);
            list.add(team);
        }
        return list;
    }

    public List<Stadium> initStadiums(JsonObject jsonObject) {
        List<Stadium> list = Lists.newArrayList();
        JsonArray jsonArray = jsonObject.get("stadiums").getAsJsonArray();
        for(int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject1 = jsonArray.get(i).getAsJsonObject();
            Stadium stadium = new Stadium(jsonObject1);
            list.add(stadium);
        }
        return list;
    }

    public List<Group> initGroups(JsonObject jsonObject) {
        List<Group> list = Lists.newArrayList();
        JsonObject js = jsonObject.get("groups").getAsJsonObject();
        String[] strings = new String[]{"a", "b", "c", "d", "e", "f", "g", "h"};
        for(int i = 0; i < strings.length; i++) {
            JsonObject jsonObject1 = js.get(strings[i]).getAsJsonObject();
            Group group = new Group(jsonObject1);
            list.add(group);
        }
        return list;
    }

    public List<Group> initKnockouts(JsonObject jsonObject) {
        List<Group> list = Lists.newArrayList();
        JsonObject js = jsonObject.get("knockout").getAsJsonObject();
        String[] strings = new String[]{"round_16", "round_8", "round_4", "round_2_loser", "round_2"};
        for(int i = 0; i < strings.length; i++) {
            JsonObject jsonObject1 = js.get(strings[i]).getAsJsonObject();
            Group group = new Group(jsonObject1, true);
            list.add(group);
        }
        return list;
    }

    public boolean groupsFinished() {
        for(Group group : Play.getInstance().getGroupList()) {
            for(Team team : group.getTeams()) {
                if(team.getPlayed() != 4) return false;
            }
        }
        return true;
    }

    public String getDay(int day) {
        int x = day-1;
        return days[x];
    }

    public void addData(GameBet gameBet) {
        DatabaseReference usersRef = ref.child(Play.getInstance().getFirebaseUser().getDisplayName());

        usersRef.child(String.valueOf(gameBet.getId())).setValue(gameBet);
    }

    public void setWMBet(TeamBet teamBet) {
        DatabaseReference usersRef = ref.child(Play.getInstance().getFirebaseUser().getDisplayName());

        usersRef.child("13012001").setValue(teamBet);
    }

    public int getBetPoints(Game game, GameBet gameBet) {
        int homeBet = gameBet.getHome();
        int awayBet = gameBet.getAway();
        int hResult = game.getHome_result();
        int aResult = game.getAway_result();
        if(game.isHadPenalty()) {
            if (game.getHome_penalty() > game.getAway_penalty())
                hResult += 1;
            else
                aResult += 1;
        }
        if(homeBet == hResult && awayBet == aResult) {
            return 3;
        } else if(homeBet-awayBet == hResult-aResult) {
            return 2;
        } else if((homeBet > awayBet && hResult > aResult) || (homeBet < awayBet && hResult < aResult)) {
            return 1;
        } else {
            return 0;
        }
    }

    public boolean timeLeft(Game game) {
        if (game.getDate().get(Calendar.DAY_OF_MONTH) < Calendar.getInstance().get(Calendar.DAY_OF_MONTH) || game.getDate().get(Calendar.MONTH) < Calendar.getInstance().get(Calendar.MONTH))
            return false;
        if (game.getDate().get(Calendar.DAY_OF_MONTH) == Calendar.getInstance().get(Calendar.DAY_OF_MONTH) && game.getDate().get(Calendar.MONTH) == Calendar.getInstance().get(Calendar.MONTH)) {
            if (game.getDate().get(Calendar.HOUR_OF_DAY) < Calendar.getInstance().get(Calendar.HOUR_OF_DAY))
                return false;
            if (game.getDate().get(Calendar.HOUR_OF_DAY) == Calendar.getInstance().get(Calendar.HOUR_OF_DAY) && game.getDate().get(Calendar.MINUTE) <= Calendar.getInstance().get(Calendar.MINUTE))
                return false;
        }
        return true;
    }

    public void setUsername() {
        if(!Play.getInstance().getFirebaseUser().getDisplayName().startsWith("Alena")) {
            getRef().child("user").child(Play.getInstance().getFirebaseUser().getDisplayName()).setValue(0);
        }
    }

    public DatabaseReference getRef() {
        return ref;
    }

}
