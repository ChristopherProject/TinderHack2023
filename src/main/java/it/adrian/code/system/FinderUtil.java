package it.adrian.code.system;

import it.adrian.code.storage.Storage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class FinderUtil {

    public static List<JSONObject> getRandomGirls( int index ) {
        List<JSONObject> girls = new ArrayList<>();
        for (int i = 0; i < index; i++) {
            JSONObject girlJson = getRandomGirl();
            if (girlJson != null) {
                girls.add(girlJson);
            }
        }
        girls.removeAll(Collections.singleton(null));
        return girls;
    }


    public static JSONObject getRandomGirl() {
        JSONObject fart = FinderUtil.getRandomGirlNonFix();
        while (fart == null) {
            fart = FinderUtil.getRandomGirlNonFix();
        }
        return fart;
    }

    private static JSONObject getRandomGirlNonFix() {
        for (JSONObject girlJson : getArrayOfRandomGirlRaw()) {
            if (girlJson != null) {
                JSONObject girlObj = new JSONObject();
                JSONObject userObj = girlJson.getJSONObject("user");
                JSONObject test = userObj.getJSONArray("photos").getJSONObject(0);
                JSONArray jsonArray = new JSONArray(test.getJSONArray("processedFiles"));
                String photoURL = jsonArray.getJSONObject(0).getString("url");
                String name = userObj.getString("name");
                String userId = userObj.getString("_id").replace("\n", "");
                girlObj.put("user_pic", photoURL);
                girlObj.put("user_id", userId);
                girlObj.put("name", name);
                return (girlObj);
            }
        }
        return null;
    }


    private static List<JSONObject> getArrayOfRandomGirlRaw() {
        List<JSONObject> girls = new ArrayList<>();
        JSONObject object = getGirls("it").get(0);
        if (!object.isEmpty() && !object.toString().contains("\"timeout\":1800000")) {
            girls.add(object.getJSONObject("data").getJSONArray("results").getJSONObject(0));
        }
        return girls;
    }

    private static List<JSONObject> getGirls( String local ) {
        final List<JSONObject> girls = new ArrayList<>();
        try {
            final URL url = new URL("https://api.gotinder.com/v2/recs/core?locale=" + local);
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("content-type", "application/json");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 13; LM-X420) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.5304.141 Mobile Safari/537.36");
            connection.setRequestProperty("X-Auth-Token", Storage.XAUTH_TOKEN);
            connection.setRequestProperty("platform", "android");
            if (connection.getResponseCode() == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String strCurrentLine;
                while ((strCurrentLine = br.readLine()) != null) {
                    stringBuilder.append(strCurrentLine);
                }
                final String data = stringBuilder.toString();
                if (!data.contains("{\"data\":{\"timeout\":1800000},\"meta\":{\"status\":200}}")) {
                    JSONObject jsonObject = new JSONObject(data);
                    girls.add(jsonObject);
                }
            } else {
                System.out.println("Bad Request! Code -> " + connection.getResponseCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return girls;
    }
}