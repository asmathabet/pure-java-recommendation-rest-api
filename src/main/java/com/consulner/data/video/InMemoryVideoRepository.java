package com.consulner.data.video;

import com.consulner.domain.video.Video;
import com.consulner.domain.video.VideoRepository;
import com.consulner.domain.video.film.Film;
import com.consulner.domain.video.series.Series;

import io.vavr.Predicates;
import io.vavr.collection.HashMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryVideoRepository implements VideoRepository {

    private final static Map<String, Video> VIDEOS_STORE = new ConcurrentHashMap();
    private final static Map<String, Film> FILMS_STORE = new ConcurrentHashMap();
    private final static Map<String, Series> SERIES_STORE = new ConcurrentHashMap();
    private final static Map<String, Video> DELETED_VIDEOS_STORE = new ConcurrentHashMap();

    @Override
    public String create(Video video) {
        VIDEOS_STORE.put(video.getId(), video);
        return video.getId();
    }

    @Override
    public String create(Film film) {
        FILMS_STORE.put(film.getId(), film);
        return film.getId();
    }

    @Override
    public String create(Series series) {
        SERIES_STORE.put(series.getId(), series);
        return series.getId();
    }

    @Override
    public ArrayList<Film> getFilms() {
        return new ArrayList<Film>(FILMS_STORE.values());
    }

    @Override
    public ArrayList<Series> getSeries() {
        return new ArrayList<Series>(SERIES_STORE.values());
    }

    @Override
    public Video findById(String id) {
        return VIDEOS_STORE.get(id) != null ? VIDEOS_STORE.get(id)
                : FILMS_STORE.get(id) != null ? FILMS_STORE.get(id) : SERIES_STORE.get(id);
    }

    @Override
    public ArrayList<Video> findByTitle(String title) {
        if (title.length() < 3)
            return new ArrayList<Video>();
        ArrayList<Video> result = VIDEOS_STORE.values().parallelStream().filter(v -> v.getTitle().contains(title))
                .collect(Collectors.toCollection(ArrayList<Video>::new));
        result.addAll(FILMS_STORE.values().parallelStream().filter(v -> v.getTitle().contains(title))
                .collect(Collectors.toCollection(ArrayList::new)));
        result.addAll(SERIES_STORE.values().parallelStream().filter(v -> v.getTitle().contains(title))
                .collect(Collectors.toCollection(ArrayList::new)));
        return result;
    }

    @Override
    public ArrayList<Video> findSimilar(String id, int deg) {
        Video video = findById(id);
        if (video == null || video.getLabels().size() == 0)
            return new ArrayList<Video>();

        ArrayList<String> labels = video.getLabels();
        ArrayList<Video> result = VIDEOS_STORE.values().parallelStream().filter(v -> {
            if (id.equals(v.getId()))
                return false;
            ArrayList<String> labs = new ArrayList<String>(v.getLabels());
            labs.retainAll(labels);
            return v.getLabels().size() >= deg;
        }).collect(Collectors.toCollection(ArrayList::new));
        result.addAll(FILMS_STORE.values().parallelStream().filter(v -> {
            if (id.equals(v.getId()))
                return false;
            ArrayList<String> labs = new ArrayList<String>(v.getLabels());
            labs.retainAll(labels);
            return v.getLabels().size() >= deg;
        }).collect(Collectors.toCollection(ArrayList::new)));
        result.addAll(SERIES_STORE.values().parallelStream().filter(v -> {
            if (id.equals(v.getId()))
                return false;
            ArrayList<String> labs = new ArrayList<String>(v.getLabels());
            labs.retainAll(labels);
            return v.getLabels().size() >= deg;
        }).collect(Collectors.toCollection(ArrayList::new)));
        return result;
    }

    @Override
    public ArrayList<Video> getVideos() {
        return new ArrayList<Video>(VIDEOS_STORE.values());
    }

    @Override
    public ArrayList<Video> getDeletedVideos() {
        return new ArrayList<Video>(DELETED_VIDEOS_STORE.values());
    }

    @Override
    public void delete(String id) {
        DELETED_VIDEOS_STORE.put(id, findById(id));
        if (VIDEOS_STORE.remove(id) == null) {
            if (FILMS_STORE.remove(id) == null) {
                SERIES_STORE.remove(id);
            }
        }
    }
}
