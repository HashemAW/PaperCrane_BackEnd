package ca.papercrane.api.repository;

import ca.papercrane.api.project.training.VideoTraining;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * A {@link JpaRepository} class pertaining to {@link VideoTraining} entities.
 */
@Repository
public interface VideoTrainingRepository extends JpaRepository<VideoTraining, Integer> {

    /**
     * Finds a video training entity by the provided videoId.
     *
     * @param videoId The id of the training being searched for.
     * @return The found optional wrapped training object otherwise empty.
     */
    Optional<VideoTraining> findByVideoId(Integer videoId);

}