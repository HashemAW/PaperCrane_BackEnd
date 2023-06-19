package ca.papercrane.api.controller;

import ca.papercrane.api.exception.ResourceNotFoundException;
import ca.papercrane.api.project.Project;
import ca.papercrane.api.service.impl.ProjectServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/projects")
public class ProjectController {

    private final ProjectServiceImpl projectService;

    /**
     * Gets all the current projects stored in the system.
     *
     * @return The list of all existing projects.
     */
    @GetMapping("")
    public ResponseEntity<List<Project>> getAll() {
        try {
            val projectList = projectService.getAll();
            return new ResponseEntity<>(projectList, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Creates a new Project.
     *
     * @param project The new project being created.
     * @return The projects generated projectId.
     */
    @PostMapping("/new")
    public ResponseEntity<Integer> createProject(@RequestBody Project project) {
        try {
            val createdProjectId = projectService.addNewProject(project);
            return new ResponseEntity<>(createdProjectId, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates an existing project.
     *
     * @param projectId The id of the project being updated.
     * @param project   The new project data.
     * @return The request response.
     */
    @PutMapping("/update/{projectId}")
    public ResponseEntity<HttpStatus> updateProject(@PathVariable Integer projectId, @RequestBody Project project) {
        try {
            projectService.update(projectId, project);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{projectId}")
    public ResponseEntity<HttpStatus> deleteProject(@PathVariable Integer projectId) {
        try {
            projectService.deleteById(projectId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Gets the project by its corresponding projectId.
     *
     * @param projectId The id of the project being retrieved.
     * @return The project data.
     */
    @GetMapping("/{projectId}")
    public ResponseEntity<Project> getProject(@PathVariable Integer projectId) {
        try {
            val project = projectService.getByProjectId(projectId);
            return new ResponseEntity<>(project, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Gets a list of all projects that for the specified clientId.
     *
     * @param clientId The id of the client that the project list is for.
     * @return The list of all projects.
     */
    @GetMapping("/user/{clientId}")
    public ResponseEntity<List<Project>> getProjectForUserId(@PathVariable Integer clientId) {
        try {
            val project = projectService.getAllByClientId(clientId);
            return new ResponseEntity<>(project, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}