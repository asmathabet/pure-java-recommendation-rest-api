package com.consulner.app.api.video.creation;

import com.consulner.app.api.Constants;
import com.consulner.app.api.Handler;
import com.consulner.app.api.ResponseEntity;
import com.consulner.app.api.StatusCode;
import com.consulner.app.errors.ApplicationExceptions;
import com.consulner.app.errors.GlobalExceptionHandler;
import com.consulner.domain.video.Video;
import com.consulner.domain.video.VideoService;
import com.consulner.domain.video.film.Film;
import com.consulner.domain.video.series.Series;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CreationHandler extends Handler {

    private final VideoService videoService;

    public CreationHandler(VideoService videoService, ObjectMapper objectMapper,
            GlobalExceptionHandler exceptionHandler) {
        super(objectMapper, exceptionHandler);
        this.videoService = videoService;
    }

    @Override
    protected void execute(HttpExchange exchange) throws IOException {
        byte[] response;
        if ("POST".equals(exchange.getRequestMethod())) {
            ResponseEntity e = doPost(exchange.getRequestBody());
            exchange.getResponseHeaders().putAll(e.getHeaders());
            exchange.sendResponseHeaders(e.getStatusCode().getCode(), 0);
            response = super.writeResponse(e.getBody());
        } else {
            throw ApplicationExceptions.methodNotAllowed(
                    "Method " + exchange.getRequestMethod() + " is not allowed for " + exchange.getRequestURI()).get();
        }

        OutputStream os = exchange.getResponseBody();
        os.write(response);
        os.close();
    }

    private ResponseEntity<CreationResponse> doPost(InputStream is) {
        CreationRequest registerRequest = super.readRequest(is, CreationRequest.class);
        String videoId = "";
        if (registerRequest.getDirector() != null || registerRequest.getReleaseDate() != null) {
            if (registerRequest.getNumberOfEpisodes() != null) {
                throw ApplicationExceptions.invalidObjectRequest(
                        "Invalid object : You eather mention attribute numberOfEpisodes or attributes director and releaseDate")
                        .get();
            } else {
                Film video = new Film(registerRequest.getId(), registerRequest.getTitle(),
                        registerRequest.getLabels(), registerRequest.getDirector(), registerRequest.getReleaseDate());
                videoId = videoService.create(video);

            }
        } else if (registerRequest.getNumberOfEpisodes() != null) {
            Series video = new Series(registerRequest.getId(), registerRequest.getTitle(), registerRequest.getLabels(),
                    registerRequest.getNumberOfEpisodes());
            videoId = videoService.create(video);

        } else {
            Video video = Video.builder()
                    .id(registerRequest.getId())
                    .title(registerRequest.getTitle())
                    .labels(registerRequest.getLabels())
                    .build();
            videoId = videoService.create(video);

        }

        CreationResponse response = new CreationResponse(videoId);

        return new ResponseEntity<>(response,
                getHeaders(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON), StatusCode.OK);
    }
}
