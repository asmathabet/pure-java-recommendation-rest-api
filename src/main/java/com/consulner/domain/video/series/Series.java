package com.consulner.domain.video.series;

import java.util.ArrayList;

import com.consulner.domain.video.Video;

import lombok.Builder;
import lombok.Data;

@Data
public class Series extends Video {
    Integer numberOfEpisodes;

    /**
     *
     * @param numberOfEpisodes numberOfEpisodes
     */
    public Series(String id, String title, ArrayList<String> labels, Integer numberOfEpisodes) {
        super(id, title, labels);
        this.numberOfEpisodes = numberOfEpisodes;
    }

}
