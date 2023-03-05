package it.adrian.code.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import it.adrian.code.system.UsersUtil;

import java.io.IOException;
import java.io.OutputStream;

public class HandlerGetLimitsForMan implements HttpHandler {

    public void handle( HttpExchange t ) throws IOException {
        String responseString = UsersUtil.getLimitationsForMan().toString();
        t.getResponseHeaders().set("Content-Type", "application/json");
        t.sendResponseHeaders(200, (responseString.getBytes()).length);
        OutputStream os = t.getResponseBody();
        os.write(responseString.getBytes());
        os.close();
    }
}