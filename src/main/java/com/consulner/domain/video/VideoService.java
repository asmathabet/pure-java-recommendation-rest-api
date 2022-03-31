package com.consulner.domain.video;

import java.util.ArrayList;
import java.util.Map;

import com.consulner.domain.video.film.Film;
import com.consulner.domain.video.series.Series;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class VideoService {

    private final VideoRepository videoRepository;

    public String create(Video video) {
        return videoRepository.create(video);
    }

    public String create(Series series) {
        return videoRepository.create(series);
    }

    public String create(Film film) {
        return videoRepository.create(film);
    }

    public void delete(String id) {
        videoRepository.delete(id);
    }

    public ArrayList<Video> getVideosStore() {
        return videoRepository.getVideos();
    }

    public ArrayList<Video> getDeletedVideosStore() {
        return videoRepository.getDeletedVideos();
    }

    public ArrayList<Film> getFilmsStore() {
        return videoRepository.getFilms();
    }

    public ArrayList<Series> getSeriesStore() {
        return videoRepository.getSeries();
    }

    public Video findById(String id) {
        return videoRepository.findById(id);
    }

    public ArrayList<Video> findByTitle(String title) {
        return videoRepository.findByTitle(title);
    }

    public ArrayList<Video> findSimilar(String id, int deg) {
        return videoRepository.findSimilar(id, deg);
    }
}
