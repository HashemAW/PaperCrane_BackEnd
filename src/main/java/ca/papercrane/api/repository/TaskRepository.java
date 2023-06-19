package ca.papercrane.api.repository;

import ca.papercrane.api.project.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * A {@link JpaRepository} class pertaining to {@link Task} entities.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

    /**
     * Finds a Task by its corresponding taskId
     *
     * @param taskId The task id being checked for an existing task.
     * @return The found optional wrapped task object otherwise empty.
     */
    Optional<Task> findByTaskId(Integer taskId);

    /**
     * Finds all tasks that belong to a specific project with the provided projectId.
     *
     * @param projectId The project id that the found list of tasks belong to.
     * @return The found optional wrapped task list otherwise empty.
     */
    Optional<List<Task>> findAllByProjectId(Integer projectId);

}