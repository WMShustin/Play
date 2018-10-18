package de.justfamouzin.play.model;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import de.justfamouzin.play.Play;

/**
 * @author Justin@Famouz
 */

public class Game {

    private int name;
    private Stadium stadium;
    private GameType gameType;
    private Calendar calendar;
    private Team home;
    private Team away;
    private String hString;
    private String aString;
    private int matchday;
    private int home_result;
    private int away_result;
    private int home_penalty;
    private int away_penalty;
    private boolean hadPenalty;
    private boolean finished;

    public Game(JsonObject jsonObject) {
        Gson gson = Play.gson;
        this.name = jsonObject.get("name").getAsInt();
        this.stadium = Play.getInstance().getStadium(jsonObject.get("stadium").getAsInt());
        this.hString = jsonObject.get("home_team").getAsString();
        this.aString = jsonObject.get("away_team").getAsString();
        try {
            this.home = Play.getInstance().getTeam(Integer.parseInt(hString));
        } catch (Exception e) {
            this.home = null;
        }
        try {
            this.away = Play.getInstance().getTeam(Integer.parseInt(aString));
        } catch (Exception e) {
            this.away = null;
        }
        String sD = jsonObject.get("date").getAsString();
        String[] sdArray = sD.split("T");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm:ss");
        this.calendar = Calendar.getInstance();
        try {
            Date date1 = simpleDateFormat.parse(sdArray[0]);
            String[] hours = sdArray[1].split("\\+");
            Date date2 = simpleDateFormat2.parse(hours[0]);
            Date date3 = simpleDateFormat2.parse(hours[1] + ":00");
            int hour = date2.getHours() - date3.getHours() + 2;
            calendar.setTime(date1);
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, date2.getMinutes());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.matchday = jsonObject.get("matchday").getAsInt();
        this.finished = jsonObject.get("finished").getAsBoolean();
        if (finished) {
            this.home_result = Integer.parseInt(jsonObject.get("home_result").getAsString());
            this.away_result = Integer.parseInt(jsonObject.get("away_result").getAsString());
            if (jsonObject.has("home_penalty")) {
                try {
                    this.home_penalty = Integer.parseInt(jsonObject.get("home_penalty").getAsString());
                    this.away_penalty = Integer.parseInt(jsonObject.get("away_penalty").getAsString());
                    this.hadPenalty = true;
                } catch (UnsupportedOperationException e) {
                    Log.d("XXX", "Game did not have penalties.");
                }
            }
        }
    }

    public Calendar getDate() {
        return calendar;
    }

    public GameType getGameType() {
        return gameType;
    }

    public int getAway_penalty() {
        return away_penalty;
    }

    public int getAway_result() {
        return away_result;
    }

    public int getHome_penalty() {
        return home_penalty;
    }

    public int getHome_result() {
        return home_result;
    }

    public int getMatchday() {
        return matchday;
    }

    public int getName() {
        return name;
    }

    public Stadium getStadium() {
        return stadium;
    }

    public Team getAway() {
        return away;
    }

    public Team getHome() {
        return home;
    }

    public String gethString() {
        return hString;
    }

    public String getaString() {
        return aString;
    }

    public boolean isFinished() {
        return finished;
    }

    public boolean isHadPenalty() {
        return hadPenalty;
    }

    public void setAway_result(int away_result) {
        this.away_result = away_result;
    }

    public void setHome_result(int home_result) {
        this.home_result = home_result;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
}

enum GameType {

    group, KO,;
}
