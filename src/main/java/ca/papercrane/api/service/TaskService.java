package ca.papercrane.api.service;

import ca.papercrane.api.project.task.Task;

import java.util.List;

public interface TaskService {

    Task getByTaskId(Integer taskId);

    List<Task> getAllByProjectId(Integer projectId);

    Integer addNewTask(Task task);

    void update(Integer taskId, Task task);

    void save(Task task);

    void deleteByTaskId(Integer taskId);

    Long totalCount();

}