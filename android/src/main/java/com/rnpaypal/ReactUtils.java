package com.rnpaypal;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class ReactUtils {

	public static WritableMap jsonObjectToWritableMap(JSONObject jsonObject) {
		if(jsonObject == null || jsonObject.length() <= 0) return null;

		WritableMap writableMap = Arguments.createMap();
		Iterator<String> iterator = jsonObject.keys();

		while(iterator.hasNext()) {
			String key = iterator.next();

			try {
				Object value = jsonObject.get(key);

				if(value == null) writableMap.putNull(key);
				else if(value instanceof Boolean) writableMap.putBoolean(key, (Boolean) value);
				else if(value instanceof Integer) writableMap.putInt(key, (Integer) value);
				else if(value instanceof Double) writableMap.putDouble(key, (Double) value);
				else if(value instanceof String) writableMap.putString(key, (String) value);
				else if(value instanceof JSONArray) writableMap.putArray(key, jsonArrayToWritableArray((JSONArray) value));
				else if(value instanceof JSONObject) writableMap.putMap(key, jsonObjectToWritableMap((JSONObject) value));
			} catch(JSONException e) {
				//TODO: Handle e
			}
		}

		return writableMap;
	}

	public static WritableArray jsonArrayToWritableArray(JSONArray jsonArray) {
		if(jsonArray == null || jsonArray.length() <= 0) return null;

		WritableArray writableArray = Arguments.createArray();

		for(int i = 0; i < jsonArray.length(); ++i) {
			try {
				Object value = jsonArray.get(i);

				if(value == null) writableArray.pushNull();
				else if(value instanceof Boolean) writableArray.pushBoolean((Boolean) value);
				else if(value instanceof Integer) writableArray.pushInt((Integer) value);
				else if(value instanceof Double) writableArray.pushDouble((Double) value);
				else if(value instanceof String) writableArray.pushString((String) value);
				else if(value instanceof JSONArray) writableArray.pushArray(jsonArrayToWritableArray((JSONArray) value));
				else if(value instanceof JSONObject) writableArray.pushMap(jsonObjectToWritableMap((JSONObject) value));
			} catch (JSONException e) {
				//TODO: Handle e
			}
		}

		return writableArray;
	}
}
