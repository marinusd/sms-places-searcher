package com.plesba.SMSsearcher;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class ResultsFetcher {
	// Get your own PLACES_API_KEY from https://code.google.com/apis/console
	private final String PLACES_API_KEY = "abcdefghijklmnopqrstuvwxyz1234567890";
	private final String DEBUG_TAG = getClass().getSimpleName().toString();

	public String getSearchResults(String msg) {
		// Log.i(DEBUG_TAG, "got search string " + msg);
		ArrayList<String> refs = getTopPlacesReferences(msg, 3); // top three //
		String ret = getPlacesDetails(refs);
		//Log.i(DEBUG_TAG, "returning " + ret);
		return ret;
	}

	private ArrayList<String> getTopPlacesReferences(String msg, int maxResults) {
		ArrayList<String> references = new ArrayList<String>();
		String noSpaces = msg.replace(' ', '+');
		String searchURL = "https://maps.googleapis.com/maps/api/place/textsearch/json?key="
				+ PLACES_API_KEY + "&sensor=false&query=" + noSpaces;
		// Log.i(DEBUG_TAG, searchURL);
		try {
			JSONObject jObject = getPlacesResult(searchURL);
			JSONArray jArray = jObject.getJSONArray("results");
			for (int i = 0; i < jArray.length() && i < maxResults; i++) {
				try {
					JSONObject oneObject = jArray.getJSONObject(i);
					references.add(oneObject.getString("reference"));
					// Log.i(DEBUG_TAG, oneObject.getString("name"));
					// Log.i(DEBUG_TAG, oneObject.getString("reference"));
				} catch (JSONException e) {
					Log.i("getTopPlacesReferences",
							"Exception " + e.getMessage());
				}
			}
		} catch (Exception e) {
			Log.i(DEBUG_TAG, "Exception " + e.getMessage());
		}
		// Log.i(DEBUG_TAG, "Returning "+ references.size() + " references");
		return references;
	}

	private String getPlacesDetails(ArrayList<String> references) {
		StringBuilder ret = new StringBuilder();
		String searchURL = "https://maps.googleapis.com/maps/api/place/details/json?key="
				+ PLACES_API_KEY + "&sensor=false&reference=";
		try {
			for (String ref : references) {
				JSONObject jObject = getPlacesResult(searchURL + ref);
				JSONObject oneObject = jObject.getJSONObject("result");
				ret.append(oneObject.getString("name") + "\n");
				// Log.i(DEBUG_TAG, "name is "+ oneObject.getString("name"));
				ret.append(oneObject.getString("formatted_address") + "\n");
				ret.append(oneObject.getString("formatted_phone_number")
						+ "\n - - - - -\n");
			}

		} catch (Exception e) {
			Log.i("GetPlacesDetails", "Exception " + e.getMessage());
		}
		// Log.i(DEBUG_TAG, "Returning "+ ret.toString());
		return ret.toString();
	}

	private JSONObject getPlacesResult(String searchURL) {
		StringBuilder sb = new StringBuilder();
		InputStream is = null;
		try {
			URL url = new URL(searchURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(10000 /* milliseconds */);
			conn.setConnectTimeout(15000 /* milliseconds */);
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			// Starts the query
			conn.connect();
			// Log.d(DEBUG_TAG, "The response is: " + response);
			is = conn.getInputStream();
			// json is UTF-8 by default
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "UTF-8"), 8);

			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (Exception e) {
			Log.i(DEBUG_TAG, "Exception " + e.getMessage());
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (Exception squish) {
			}

		}
		try {
			return new JSONObject(sb.toString());
		} catch (JSONException e) {
			e.printStackTrace();
			return new JSONObject();
		}
	}
}
