package it.adrian.code.handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import it.adrian.code.system.FinderUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public final class HandlerGetGirls implements HttpHandler {
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

    public void handle(HttpExchange t) throws IOException {
        String query = t.getRequestURI().getQuery();
        Map<String, String> queryParams = parseQuery(query);

        String responseJson;
        if (query.contains("index") && !(queryParams.get("index") == null || queryParams.get("index").equals(""))) {
            JSONArray okei = new JSONArray();
            try {
                for (JSONObject json : FinderUtil.getRandomGirls(Integer.parseInt(queryParams.get("index")))) {
                    okei.put(json);
                }
                responseJson = okei.toString();
            } catch (NumberFormatException e) {
                responseJson = "{\"error\": \"Invalid value for parameter 'index'\"}";
            }
        } else {
            JSONObject randomGirl = FinderUtil.getRandomGirl();
            if (randomGirl != null) {
                responseJson = randomGirl.toString();
            } else {
                responseJson = "{\"error\": \"No random girl found\"}";
            }
        }

        Headers headers = t.getResponseHeaders();
        headers.set("Content-Type", "application/json");
        t.sendResponseHeaders(200, responseJson.getBytes().length);
        OutputStream os = t.getResponseBody();
        os.write(responseJson.getBytes());
        os.close();
    }
}