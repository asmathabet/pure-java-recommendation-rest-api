package com.consulner.app.api.video.search;

import com.consulner.domain.video.Video;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
class SearchResponse {

    Video video;
}