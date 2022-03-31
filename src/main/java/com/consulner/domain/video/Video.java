package com.consulner.domain.video;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Video {

    String id;
    String title;
    ArrayList<String> labels;
}
