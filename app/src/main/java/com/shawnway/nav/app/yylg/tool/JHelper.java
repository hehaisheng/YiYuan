package com.shawnway.nav.app.yylg.tool;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JHelper {
	@SuppressWarnings("unchecked")
	public static <T extends Object> T get(JSONObject json, String name) {
		Object value;
		if (json==null ) {
			Log.e("JSONHelper", "json is null,name is"+name);
			return null;
		}
		try {
			value = json.get(name);
			return (T) value;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

	}
}
