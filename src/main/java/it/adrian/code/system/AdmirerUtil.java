package it.adrian.code.system;

import it.adrian.code.storage.Storage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public final class AdmirerUtil {

    public static List<JSONObject> getSecretAdmirer() {
        List<JSONObject> admirer = new ArrayList<>();
        if (getWhoLikesYou().size() != 0) {
            List<JSONObject> photoList = new ArrayList<>();
            for (JSONObject slut : getWhoLikesYou()) {
                JSONArray photos = slut.getJSONObject("user").getJSONArray("photos");
                for (int i = 0; i < photos.length(); i++) {
                    JSONObject photo = photos.getJSONObject(i);
                    photoList.add(photo);
                }
            }
            if (!photoList.isEmpty()) {
                photoList.forEach(ez -> {
                    JSONObject user = new JSONObject(UsersUtil.fetchUser(ez.getString("url").substring(32, 56)));
                    JSONObject photo_user = user.getJSONObject("results").getJSONArray("photos").getJSONObject(0);
                    JSONArray jsonArray = new JSONArray(photo_user.getJSONArray("processedFiles"));
                    String photoURL = jsonArray.getJSONObject(0).getString("url");
                    String userID = user.getJSONObject("results").getString("_id");
                    JSONObject girlObj = new JSONObject();
                    girlObj.put("photo", photoURL);
                    girlObj.put("user_id", userID);
                    admirer.add(girlObj);
                });
            }
        }
        return admirer;
    }

    private static List<JSONObject> getWhoLikesYou() {
        List<JSONObject> list = new ArrayList<>();
        try {
            final URL url = new URL("https://api.gotinder.com/v2/fast-match/teasers");
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
                final JSONObject jsonObject = new JSONObject(data);
                JSONArray array = jsonObject.getJSONObject("data").getJSONArray("results");

                for (int i = 0; i < array.length(); i++) {
                    list.add(array.getJSONObject(i));
                }
            } else {
                System.out.println("Bad Request! Code -> " + connection.getResponseCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}