package it.adrian.code.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import it.adrian.code.system.UsersUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public final class HandlerSendMessage implements HttpHandler {
    private static Map<String, String> parseQuery( String query) {
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

    public void handle( HttpExchange t) throws IOException {
        String query = t.getRequestURI().getQuery();
        Map<String, String> queryParams = parseQuery(query);
        String responseString =  UsersUtil.sendMessage(queryParams.get("matchID"), queryParams.get("message"));
        t.getResponseHeaders().set("Content-Type", "application/json");
        t.sendResponseHeaders(200, (responseString.getBytes()).length);
        OutputStream os = t.getResponseBody();
        os.write(responseString.getBytes());
        os.close();
    }
}
