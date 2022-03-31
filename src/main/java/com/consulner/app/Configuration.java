package com.consulner.app;

import com.consulner.app.errors.GlobalExceptionHandler;
import com.consulner.data.video.InMemoryVideoRepository;
import com.consulner.domain.video.VideoRepository;
import com.consulner.domain.video.VideoService;
import com.fasterxml.jackson.databind.ObjectMapper;

class Configuration {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final VideoRepository VIDEO_REPOSITORY = new InMemoryVideoRepository();
    private static final VideoService VIDEO_SERVICE = new VideoService(VIDEO_REPOSITORY);
    private static final GlobalExceptionHandler GLOBAL_ERROR_HANDLER = new GlobalExceptionHandler(OBJECT_MAPPER);

    static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

    static VideoService getVideoService() {
        return VIDEO_SERVICE;
    }

    static VideoRepository getVideoRepository() {
        return VIDEO_REPOSITORY;
    }

    public static GlobalExceptionHandler getErrorHandler() {
        return GLOBAL_ERROR_HANDLER;
    }
}
