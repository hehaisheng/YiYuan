package com.shawnway.nav.app.yylg.tool;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by Eiffel on 2016/3/24.
 */
public class JsonUtil {
    /**
     * 将Map转化为Json
     *
     * @param map
     * @return String
     */
    public static <T> String mapToJson(Map<String, T> map) {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(map);
        return jsonStr;
    }


    public static Map<String, String> jsonToMap(String jsonString) {
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        Gson gson = new Gson();
        return gson.fromJson(jsonString, type);
    }
}
