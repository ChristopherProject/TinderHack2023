package it.adrian.code.system;

import it.adrian.code.storage.Storage;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public final class UsersUtil {

    public static String fetchUser( String id ) {
        try {
            URL url = new URL("https://api.gotinder.com/user/" + id);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 13; LM-X420) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.5304.141 Mobile Safari/537.36");
            conn.setRequestProperty("X-Auth-Token", Storage.XAUTH_TOKEN);
            conn.setRequestProperty("platform", "android");

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new RuntimeException("Failed to fetch user " + id + ", response code: " + responseCode);
            }

            return new Scanner(conn.getInputStream(), StandardCharsets.UTF_8).useDelimiter("\\A").next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public static JSONObject sendLikeOrDislike( String like_or_pass, String id ) {
        try {
            final URL url = new URL("https://api.gotinder.com/" + like_or_pass + "/" + id + "?locale=it");
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("content-type", "application/json");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 13; LM-X420) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.5304.141 Mobile Safari/537.36");
            connection.setRequestProperty("X-Auth-Token", Storage.XAUTH_TOKEN);
            connection.setRequestProperty("platform", "android");

            if (connection.getResponseCode() == 200) {
                // System.out.println("Accepted Request!..");
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String strCurrentLine;
                while ((strCurrentLine = br.readLine()) != null) {
                    stringBuilder.append(strCurrentLine);
                }
                final String data = stringBuilder.toString();
                return new JSONObject(data);
               // System.out.println("Likes Remaining For Today: " + jsonObject.getInt("likes_remaining"));
            } else {
               // System.out.println("Bad Request! Code -> " + connection.getResponseCode());
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return new JSONObject();
    }


}
