package de.baumann.browser.objects;

import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

import de.baumann.browser.activity.BrowserActivity;

public class CustomRedirectsHelper {

    public final static String CUSTOM_REDIRECTS_KEY = "customRedirects";

    public static ArrayList<CustomRedirect> getRedirects(SharedPreferences preferences) throws JSONException {
        ArrayList<CustomRedirect> redirects = new ArrayList<>();
        String redirectsPref = preferences.getString(CUSTOM_REDIRECTS_KEY, "[]");

        if (Objects.requireNonNull(preferences.getString("saved_redirect_ok", "no")).equals("no")) {
            redirects.add(new CustomRedirect("m.youtube.com", preferences.getString("sp_youTube_string_domain", "invidious.nerdvpn.de")));
            redirects.add(new CustomRedirect("youtube.com", preferences.getString("sp_youTube_string_domain", "invidious.nerdvpn.de")));
            redirects.add(new CustomRedirect("twitter.com", preferences.getString("sp_twitter_string_domain", "nitter.net")));
            saveRedirects(redirects);
            preferences.edit().putString("saved_redirect_ok", "yes").apply();
        }

        JSONArray array = new JSONArray(redirectsPref);
        for (int i = 0; i < array.length(); i++) {
            JSONObject redirect = array.getJSONObject(i);
            String source = redirect.getString("source");
            String target = redirect.getString("target");
            redirects.add(new CustomRedirect(source, target));
            redirects.sort(Comparator.comparing(CustomRedirect::getSource));
        }
        return redirects;
    }

    public static void saveRedirects(ArrayList<CustomRedirect> redirects) throws JSONException {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(BrowserActivity.getAppContext());
        JSONArray array = new JSONArray();
        for (int i = 0; i < redirects.size(); i++) {
            CustomRedirect redirect = redirects.get(i);
            JSONObject object = new JSONObject();
            object.put("source", redirect.getSource());
            object.put("target", redirect.getTarget());
            array.put(object);
        }
        preferences.edit().putString(CUSTOM_REDIRECTS_KEY, array.toString()).apply();
    }
}
