package ca.papercrane.api.service;

import ca.papercrane.api.project.training.VideoTraining;

import java.util.List;

public interface VideoTrainingService {

    List<VideoTraining> getAll();

    VideoTraining getByVideoId(Integer videoId);

    Integer addNewTraining(VideoTraining training);

    void update(Integer videoId, VideoTraining training);

    void save(VideoTraining training);

    void deleteByVideoId(Integer videoId);

    Long totalCount();

}