package it.adrian.code.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import it.adrian.code.system.AdmirerUtil;
import it.adrian.code.system.FinderUtil;
import it.adrian.code.system.UsersUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HandlerMatchsIDs implements HttpHandler {
    private static Map<String, String> parseQuery( String query ) {
        Map<String, String> result = new HashMap<>();
        if (query != null)
            for (String param : query.split("&")) {
                String[] pair = param.split("=");
                String key = URLDecoder.decode(pair[0], StandardCharsets.UTF_8);
                String value = (pair.length > 1) ? URLDecoder.decode(pair[1], StandardCharsets.UTF_8) : "";
                result.put(key, value);
            }
        return result;
    }

    public void handle( HttpExchange t ) throws IOException {
        String query = t.getRequestURI().getQuery();
        Map<String, String> queryParams = parseQuery(query);

        String responseJson;
        if (query.contains("count")) {
            try {
                int count = Integer.parseInt(queryParams.get("count"));
                JSONArray matchIds = new JSONArray();
                for (String data : AdmirerUtil.getYourMatchIDs(count)) {
                    matchIds.put(new JSONObject("{\"matchID\": \" "+data+"\"}"));
                }
                responseJson = matchIds.toString();
                t.sendResponseHeaders(200, responseJson.length());
            } catch (NumberFormatException e) {
                responseJson = "{\"error\": \"Invalid value for parameter 'count'\"}";
                t.sendResponseHeaders(400, responseJson.length());
            } catch (Exception e) {
                responseJson = "{\"error\": \"" + e.getMessage() + "\"}";
                t.sendResponseHeaders(500, responseJson.length());
            }
        } else {
            responseJson = "{\"error\": \"Missing required parameter 'count'\"}";
            t.sendResponseHeaders(400, responseJson.length());
        }

        t.getResponseHeaders().set("Content-Type", "application/json");
        OutputStream os = t.getResponseBody();
        os.write(responseJson.getBytes());
        os.close();
    }
}