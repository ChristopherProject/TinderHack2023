package it.adrian.code;

import com.sun.net.httpserver.HttpServer;
import it.adrian.code.handler.HandlerGetGirls;
import it.adrian.code.handler.HandlerGetSecretAdmirer;
import it.adrian.code.handler.HandlerLikesAndDislikes;

import java.net.InetSocketAddress;

public class Main {

    public static void main(String... args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/api/decision", new HandlerLikesAndDislikes());//http://localhost:8000/api/decision?action=like&id=63c8570e35ce8401000b6e5d
        server.createContext("/api/search", new HandlerGetGirls()); //http://localhost:8000/api/search
        server.createContext("/api/admirers", new HandlerGetSecretAdmirer());//http://localhost:8000/api/admirers
        server.setExecutor(null);
        server.start();
        System.out.println("Server started on port 8000");
        System.runFinalization();
        System.gc();
    }
}