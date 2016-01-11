/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parallax.server.blocklypropauth.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 *
 * @author Michel
 */
public class JsonUtils {

    public static JsonObject createJsonFailure() {
        JsonObject result = new JsonObject();
        result.addProperty("success", Boolean.FALSE);
        return result;
    }

    public static JsonObject createJsonFailure(String message) {
        JsonObject result = new JsonObject();
        result.addProperty("success", Boolean.FALSE);
        result.addProperty("message", message);
        return result;
    }

    public static JsonObject createJsonSuccess(JsonElement data) {
        JsonObject result = new JsonObject();
        result.addProperty("success", Boolean.TRUE);
        result.add("data", data);
        return result;
    }

}
