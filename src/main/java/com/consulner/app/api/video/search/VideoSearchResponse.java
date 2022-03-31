package com.consulner.app.api.video.search;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
class VideoSearchResponse {

    String id;
    String title;
    ArrayList<String> labels;
}