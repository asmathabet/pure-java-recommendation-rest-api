package com.consulner.domain.video;

import java.util.ArrayList;

import com.consulner.domain.video.film.Film;
import com.consulner.domain.video.series.Series;

public interface VideoRepository {

    String create(Video video);

    String create(Film film);

    String create(Series series);

    void delete(String id);

    ArrayList<Series> getSeries();

    ArrayList<Film> getFilms();

    ArrayList<Video> getVideos();

    ArrayList<Video> getDeletedVideos();

    Video findById(String id);

    ArrayList<Video> findByTitle(String title);

    ArrayList<Video> findSimilar(String id, int deg);

}
