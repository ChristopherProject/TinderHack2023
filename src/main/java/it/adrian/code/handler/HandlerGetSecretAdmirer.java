package it.adrian.code.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import it.adrian.code.system.AdmirerUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;

public final class HandlerGetSecretAdmirer implements HttpHandler {

    public void handle( HttpExchange t) throws IOException {
        JSONArray okei = new JSONArray();
        for (JSONObject json : AdmirerUtil.getSecretAdmirer()) {
            okei.put(json);
        }

        String responseString = okei.toString();
        t.getResponseHeaders().set("Content-Type", "application/json");
        t.sendResponseHeaders(200, (responseString.getBytes()).length);
        OutputStream os = t.getResponseBody();
        os.write(responseString.getBytes());
        os.close();
    }
}
