package ca.papercrane.api.repository;

import ca.papercrane.api.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * A {@link JpaRepository} class pertaining to {@link Project} entities.
 */
@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {

    /**
     * Finds a project by its corresponding projectId
     *
     * @param projectId The project id being checked for an existing project.
     * @return The found optional wrapped project object otherwise empty.
     */
    Optional<Project> findByProjectId(Integer projectId);

    /**
     * Finds all projects that a client with the specified clientId may be attached to.
     *
     * @param clientId The client id used to find the list of projects.
     * @return The found optional wrapped project list otherwise empty.
     */
    Optional<List<Project>> findAllByClientId(Integer clientId);

}