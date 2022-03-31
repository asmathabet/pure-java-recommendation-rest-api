package com.consulner.app.api.video.creation;

import java.util.ArrayList;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

@Value
class CreationRequest {
    String id;
    String title;
    String director;
    @Getter(AccessLevel.NONE)
    String release_date;
    @Getter(AccessLevel.NONE)
    Integer number_of_episodes;
    ArrayList<String> labels;

    /**
     * @return String
     */
    public String getReleaseDate() {
        return this.release_date;
    }

    /**
     * @return Integer
     */
    public Integer getNumberOfEpisodes() {
        return this.number_of_episodes;
    }
}
