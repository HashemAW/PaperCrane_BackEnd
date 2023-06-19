package ca.papercrane.api.service.impl;

import ca.papercrane.api.exception.ResourceNotFoundException;
import ca.papercrane.api.project.training.VideoTraining;
import ca.papercrane.api.repository.VideoTrainingRepository;
import ca.papercrane.api.service.VideoTrainingService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VideoTrainingServiceImpl implements VideoTrainingService {

    private final VideoTrainingRepository trainingRepository;

    @Override
    public List<VideoTraining> getAll() {
        val trainingList = trainingRepository.findAll();
        if (trainingList.isEmpty()) {
            throw new ResourceNotFoundException("No training found.");
        }
        return trainingList;
    }

    @Override
    public VideoTraining getByVideoId(Integer videoId) {
        return trainingRepository.findByVideoId(videoId).orElseThrow(() -> new ResourceNotFoundException("Training not found for id: " + videoId));
    }

    @Override
    public Integer addNewTraining(VideoTraining training) {
        val savedTraining = trainingRepository.save(training);
        return savedTraining.getVideoId();
    }

    @Override
    public void update(Integer videoId, VideoTraining training) {
        val existingTraining = getByVideoId(videoId);
        existingTraining.setProjectId(training.getProjectId());
        existingTraining.setDescription(training.getDescription());
        existingTraining.setVideoLink(training.getVideoLink());
        save(existingTraining);
    }

    @Override
    public void save(VideoTraining training) {
        trainingRepository.save(training);
    }

    @Override
    public void deleteByVideoId(Integer videoId) {
        trainingRepository.findByVideoId(videoId).ifPresentOrElse(trainingRepository::delete, () -> {
            throw new ResourceNotFoundException("Training not found for video ID: " + videoId);
        });
    }

    @Override
    public Long totalCount() {
        return trainingRepository.count();
    }

}