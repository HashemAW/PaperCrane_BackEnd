package ca.papercrane.api.controller;

import ca.papercrane.api.exception.ResourceNotFoundException;
import ca.papercrane.api.project.training.VideoTraining;
import ca.papercrane.api.service.impl.VideoTrainingServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/training")
public class VideoTrainingController {

    private final VideoTrainingServiceImpl trainingService;

    @GetMapping("")
    public ResponseEntity<List<VideoTraining>> getAll() {
        try {
            val trainingList = trainingService.getAll();
            return new ResponseEntity<>(trainingList, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Creates a new VideoTraining.
     *
     * @param training The new training being created.
     * @return The trainings generated video id.
     */
    @PostMapping("/create")
    public ResponseEntity<Integer> createTraining(@RequestBody VideoTraining training) {
        try {
            val createdTrainingId = trainingService.addNewTraining(training);
            return new ResponseEntity<>(createdTrainingId, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates an existing Video Training.
     *
     * @param videoId  The id of the existing training.
     * @param training The new training details.
     * @return The response of the request.
     */
    @PutMapping("/update/{requestId}")
    public ResponseEntity<HttpStatus> updateTraining(@PathVariable Integer videoId, @RequestBody VideoTraining training) {
        try {
            trainingService.update(videoId, training);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a VideoTraining entry by its corresponding videoId.
     *
     * @param videoId The id of the training being deleted.
     * @return The response status.
     */
    @DeleteMapping("/delete/{requestId}")
    public ResponseEntity<HttpStatus> deleteTraining(@PathVariable Integer videoId) {
        try {
            trainingService.deleteByVideoId(videoId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<VideoTraining> getTraining(@PathVariable Integer id) {
        try {
            val videoTraining = trainingService.getByVideoId(id);
            return new ResponseEntity<>(videoTraining, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}