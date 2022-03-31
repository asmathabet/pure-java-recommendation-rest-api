package com.consulner.app.api.video.search;

import static com.consulner.app.api.ApiUtils.splitQuery;

import com.consulner.app.api.Constants;
import com.consulner.app.api.Handler;
import com.consulner.app.api.ResponseEntity;
import com.consulner.app.api.StatusCode;
import com.consulner.app.api.video.search.VideoSearchResponse;
import com.consulner.app.errors.ApplicationExceptions;
import com.consulner.app.errors.GlobalExceptionHandler;
import com.consulner.domain.video.Video;
import com.consulner.domain.video.VideoService;
import com.consulner.domain.video.film.Film;
import com.consulner.domain.video.series.Series;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchHandler extends Handler {

        private final VideoService videoService;

        public SearchHandler(VideoService videoService, ObjectMapper objectMapper,
                        GlobalExceptionHandler exceptionHandler) {
                super(objectMapper, exceptionHandler);
                this.videoService = videoService;
        }

        @Override
        protected void execute(HttpExchange exchange) throws IOException {
                byte[] response;
                if ("GET".equals(exchange.getRequestMethod())) {
                        Map<String, List<String>> params = splitQuery(exchange.getRequestURI().getRawQuery());
                        String path = exchange.getRequestURI().getPath();
                        ResponseEntity e = path.equals("/api/videos/get") ? doGet(params.get("id").get(0))
                                        : path.equals("/api/videos/find-title")
                                                        ? doGetByTitle(params.get("title").get(0))
                                                        : path.equals("/api/videos/find-similar")
                                                                        ? doGetSimilar(params.get("id").get(0),
                                                                                        Integer.parseInt(params
                                                                                                        .get("deg")
                                                                                                        .get(0)))
                                                                        : path.equals("/api/videos/get-films")
                                                                                        ? doGetFilms()
                                                                                        : path.equals("/api/videos/get-series")
                                                                                                        ? doGetSeries()
                                                                                                        : path.equals("/api/videos/get-deleted")
                                                                                                                        ? doGetDeleted()
                                                                                                                        : null;
                        exchange.getResponseHeaders().putAll(e.getHeaders());
                        exchange.sendResponseHeaders(e.getStatusCode().getCode(), 0);
                        response = super.writeResponse(e.getBody());
                } else {
                        throw ApplicationExceptions.methodNotAllowed(
                                        "Method " + exchange.getRequestMethod() + " is not allowed for "
                                                        + exchange.getRequestURI())
                                        .get();
                }

                OutputStream os = exchange.getResponseBody();
                os.write(response);
                os.close();
        }

        private ResponseEntity<SearchResponse> doGet(String id) {
                Video video = videoService.findById(id);
                SearchResponse response = SearchResponse.builder().video(video).build();
                return new ResponseEntity<SearchResponse>(response,
                                getHeaders(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON), StatusCode.OK);
        }

        private ResponseEntity<ArrayList<SearchResponse>> doGetByTitle(String title) {
                ArrayList<Video> videos = videoService.findByTitle(title);
                ArrayList<SearchResponse> response = new ArrayList<SearchResponse>();
                videos.forEach(v -> response.add(SearchResponse.builder().video(v).build()));
                return new ResponseEntity<ArrayList<SearchResponse>>(response,
                                getHeaders(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON), StatusCode.OK);
        }

        private ResponseEntity<ArrayList<SearchResponse>> doGetSimilar(String id, int deg) {
                ArrayList<Video> videos = videoService.findSimilar(id, deg);

                ArrayList<SearchResponse> response = new ArrayList<SearchResponse>();
                videos.forEach(v -> response.add(SearchResponse.builder().video(v).build()));

                return new ResponseEntity<ArrayList<SearchResponse>>(response,
                                getHeaders(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON), StatusCode.OK);
        }

        private ResponseEntity<ArrayList<SearchResponse>> doGetFilms() {
                ArrayList<SearchResponse> response = new ArrayList<SearchResponse>();
                videoService.getFilmsStore().parallelStream()
                                .forEach(v -> response.add(SearchResponse.builder().video(v).build()));

                return new ResponseEntity<ArrayList<SearchResponse>>(response,
                                getHeaders(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON), StatusCode.OK);
        }

        private ResponseEntity doGetSeries() {
                ArrayList<SearchResponse> response = new ArrayList<SearchResponse>();
                videoService.getSeriesStore().parallelStream()
                                .forEach(v -> response.add(SearchResponse.builder().video(v).build()));

                return new ResponseEntity<ArrayList<SearchResponse>>(response,
                                getHeaders(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON), StatusCode.OK);
        }

        private ResponseEntity<ArrayList<SearchResponse>> doGetDeleted() {
                ArrayList<SearchResponse> response = new ArrayList<SearchResponse>();
                videoService.getDeletedVideosStore()
                                .forEach(v -> response.add(SearchResponse.builder().video(v).build()));

                return new ResponseEntity<ArrayList<SearchResponse>>(response,
                                getHeaders(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON), StatusCode.OK);
        }
}
