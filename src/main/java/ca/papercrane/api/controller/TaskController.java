package ca.papercrane.api.controller;

import ca.papercrane.api.exception.ResourceNotFoundException;
import ca.papercrane.api.project.task.Task;
import ca.papercrane.api.service.impl.TaskServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/projects/tasks")
public class TaskController {

    private final TaskServiceImpl taskService;

    /**
     * Gets a list of every task that belongs to a specific project id.
     *
     * @param projectId The id of the project that the list of tasks is for.
     * @return The list.
     */
    @GetMapping("/{projectId}/tasks")
    public ResponseEntity<List<Task>> taskByProjectId(@PathVariable Integer projectId) {
        try {
            val projectTaskList = taskService.getAllByProjectId(projectId);
            return new ResponseEntity<>(projectTaskList, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Creates a new Task.
     *
     * @param task The new task being created.
     * @return The tasks generated task id.
     */
    @PostMapping("/create")
    public ResponseEntity<Integer> createTask(@RequestBody Task task) {
        try {
            val createdRequestId = taskService.addNewTask(task);
            return new ResponseEntity<>(createdRequestId, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates an existing Task.
     *
     * @param taskId The id of the existing task.
     * @param task   The new task details.
     * @return The response of the request.
     */
    @PutMapping("/update/{requestId}")
    public ResponseEntity<HttpStatus> updateTask(@PathVariable Integer taskId, @RequestBody Task task) {
        try {
            taskService.update(taskId, task);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a Task by its corresponding taskId.
     *
     * @param taskId The id of the task being deleted.
     * @return The response status.
     */
    @DeleteMapping("/delete/{requestId}")
    public ResponseEntity<HttpStatus> deleteTask(@PathVariable Integer taskId) {
        try {
            taskService.deleteByTaskId(taskId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * Gets a task by it's id value.
     *
     * @param taskId The task id to search for.
     * @return The found task data.
     */
    @GetMapping("/{taskId}")
    public ResponseEntity<Task> taskByTaskId(@PathVariable Integer taskId) {
        try {
            val task = taskService.getByTaskId(taskId);
            return new ResponseEntity<>(task, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}