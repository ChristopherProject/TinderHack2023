package it.adrian.code;

import com.sun.net.httpserver.HttpServer;
import it.adrian.code.handler.*;

import java.net.InetSocketAddress;

public class Main {

    public static void main(String... args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/api/decision", new HandlerLikesAndDislikes());//http://localhost:8000/api/decision?action=like&id=63c8570e35ce8401000b6e5d
        server.createContext("/api/search", new HandlerGetGirls()); //http://localhost:8000/api/search
        server.createContext("/api/admirers", new HandlerGetSecretAdmirer());//http://localhost:8000/api/admirers
        server.createContext("/api/sendMessage", new HandlerSendMessage());//http://localhost:8000/api/sendMessage?matchID=63696974c2236b0100ade87563979ae3073383010001fd3a&message=hello
        server.createContext("/api/getMatchs", new HandlerMatchsIDs());//http://localhost:8000/api/getMatchs?count=60
        server.setExecutor(null);
        server.start();
        System.out.println("Server started on port 8000");
        System.runFinalization();
        System.gc();
    }
}