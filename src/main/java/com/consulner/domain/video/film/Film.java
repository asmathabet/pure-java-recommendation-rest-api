package com.consulner.domain.video.film;

import java.util.ArrayList;

import com.consulner.domain.video.Video;

import lombok.Builder;
import lombok.Data;

@Data
public class Film extends Video {
    String director;
    String releaseDate;

    /**
     *
     * @param director    director
     * @param releaseDate releaseDate
     */
    public Film(String id, String title, ArrayList<String> labels, String director, String releaseDate) {
        super(id, title, labels);
        this.director = director;
        this.releaseDate = releaseDate;
    }

}
