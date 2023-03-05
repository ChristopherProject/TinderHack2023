package it.adrian.code.system;

import it.adrian.code.storage.Storage;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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

    public static String sendMessage( String matchID, String message ) {
        try {
            URL url = new URL("https://api.gotinder.com/user/matches/" + matchID + "?locale=it");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");

            con.setRequestProperty("content-type", "application/json");
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 13; LM-X420) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.5304.141 Mobile Safari/537.36");
            con.setRequestProperty("X-Auth-Token", Storage.XAUTH_TOKEN);
            con.setRequestProperty("platform", "android");

            String body = new StringBuilder().append("{\"message\": \"").append(message).append("\"}").toString();
            con.setDoOutput(true);
            OutputStream os = con.getOutputStream();
            os.write(body.getBytes());
            os.flush();
            os.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Instant getWhenRefilYourLikes() {
        try {
            URL url = new URL("https://api.gotinder.com/v2/profile?locale=it&include=likes");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 13; LM-X420) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.5304.141 Mobile Safari/537.36");
            conn.setRequestProperty("X-Auth-Token", Storage.XAUTH_TOKEN);
            conn.setRequestProperty("platform", "android");

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new RuntimeException("this request has been failed, response code: " + responseCode);
            } else {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String strCurrentLine;
                while ((strCurrentLine = br.readLine()) != null) {
                    stringBuilder.append(strCurrentLine);
                }
                final String data = stringBuilder.toString();
                long rateLimitUntil = new JSONObject(data).getJSONObject("data").getJSONObject("likes").getLong("rate_limited_until");
                Instant instant = Instant.ofEpochMilli(rateLimitUntil);
                return instant;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static int getNumberOfLike() {
        try {
            URL url = new URL("https://api.gotinder.com/v2/profile?locale=it&include=likes");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 13; LM-X420) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.5304.141 Mobile Safari/537.36");
            conn.setRequestProperty("X-Auth-Token", Storage.XAUTH_TOKEN);
            conn.setRequestProperty("platform", "android");

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new RuntimeException("this request has been failed, response code: " + responseCode);
            } else {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String strCurrentLine;
                while ((strCurrentLine = br.readLine()) != null) {
                    stringBuilder.append(strCurrentLine);
                }
                final String data = stringBuilder.toString();

                return new JSONObject(data).getJSONObject("data").getJSONObject("likes").getInt("likes_remaining");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
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
            }
        } catch (Exception ignored) {
        }
        return new JSONObject();
    }

    public static JSONObject getLimitationsForMan() {
        String jsonPenality;
        final int number = getNumberOfLike();
        if (number == 0) {
            Instant refilTime = getWhenRefilYourLikes();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss");
            assert refilTime != null;
            String formattedDate = formatter.format(refilTime.atZone(ZoneId.systemDefault()));
            Instant now = Instant.now();
            Duration duration = Duration.between(now, refilTime);
            long seconds = Math.abs(duration.getSeconds());
            long hours = seconds / 3600;
            long minutes = (seconds % 3600) / 60;
            long remainingSeconds = seconds % 60;
            String durationStr = String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds);
            jsonPenality = "{\n" + "   \"refilDate\": \"" + formattedDate + "\",\n" + "   \"timeRemaining\": \"" + durationStr + "\"\n" + "}";
        } else {
            jsonPenality = "{\n" + "   \"number_of_likes_remaining\": " + number + "\n" + "}";
        }
        return (new JSONObject(jsonPenality));
    }
}