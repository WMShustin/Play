package de.justfamouzin.play.model;

import com.google.gson.JsonObject;

/**
 * @author Justin@Famouz
 */

public class Stadium {

    private int id;
    private String name;
    private String city;

    public Stadium(JsonObject jsonObject) {
        this.id = jsonObject.get("id").getAsInt();
        this.name = jsonObject.get("name").getAsString();
        this.city = jsonObject.get("city").getAsString();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "ID: " + id + " || " + "NAME: " + name + " || " + "CITY: " + city;
    }
}
