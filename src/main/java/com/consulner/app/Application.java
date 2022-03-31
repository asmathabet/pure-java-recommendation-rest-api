package com.consulner.app;

import static com.consulner.app.Configuration.getErrorHandler;
import static com.consulner.app.Configuration.getObjectMapper;
import static com.consulner.app.Configuration.getVideoService;

import com.consulner.app.api.video.creation.CreationHandler;
import com.consulner.app.api.video.delete.DeleteHandler;
import com.consulner.app.api.video.search.SearchHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

public class Application {

    public static void main(String[] args) throws IOException {
        int serverPort = 8000;
        HttpServer server;
        server = HttpServer.create(new InetSocketAddress(serverPort), 0);
        CreationHandler creationHandler = new CreationHandler(getVideoService(), getObjectMapper(),
                getErrorHandler());
        SearchHandler searchHandler = new SearchHandler(getVideoService(), getObjectMapper(),
                getErrorHandler());
        DeleteHandler deleteHandler = new DeleteHandler(getVideoService(), getObjectMapper(),
                getErrorHandler());
        server.createContext("/api/videos/add", creationHandler::handle);
        server.createContext("/api/videos/get", searchHandler::handle);
        server.createContext("/api/videos/find-title", searchHandler::handle);
        server.createContext("/api/videos/find-similar", searchHandler::handle);
        server.createContext("/api/videos/get-films", searchHandler::handle);
        server.createContext("/api/videos/get-series", searchHandler::handle);
        server.createContext("/api/videos/get-deleted", searchHandler::handle);
        server.createContext("/api/videos/delete", deleteHandler::handle);

        server.setExecutor(null); // creates a default executor
        server.start();
    }
}
