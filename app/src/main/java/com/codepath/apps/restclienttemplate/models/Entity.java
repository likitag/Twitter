package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;



public class Entity {
    public String media;
    public static Entity fromJson(JSONObject jsonObject) throws JSONException {
        Entity entity = new Entity();
        entity.media = jsonObject.getString("media");
        return entity;

    }
}
