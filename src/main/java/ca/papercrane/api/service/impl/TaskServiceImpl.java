package ca.papercrane.api.service.impl;

import ca.papercrane.api.exception.ResourceNotFoundException;
import ca.papercrane.api.project.task.Task;
import ca.papercrane.api.repository.TaskRepository;
import ca.papercrane.api.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    public Task getByTaskId(Integer taskId) {
        return taskRepository.findByTaskId(taskId).orElseThrow(() -> new ResourceNotFoundException("Task not found with taskId: " + taskId));
    }

    @Override
    public List<Task> getAllByProjectId(Integer projectId) {
        return taskRepository.findAllByProjectId(projectId).orElseThrow(() -> new ResourceNotFoundException("No tasks found for projectId: " + projectId));
    }

    @Override
    public Integer addNewTask(Task task) {
        val createdTask = taskRepository.save(task);
        return createdTask.getTaskId();
    }

    @Override
    public void update(Integer taskId, Task task) {
        val existingTask = getByTaskId(taskId);
        existingTask.setDescription(task.getDescription());
        existingTask.setStartDate(task.getStartDate());
        existingTask.setDeadline(task.getDeadline());
        existingTask.setExpectedWorkHours(task.getExpectedWorkHours());
        save(existingTask);
    }

    @Override
    public void save(Task task) {
        taskRepository.save(task);
    }

    @Override
    public void deleteByTaskId(Integer taskId) {
        taskRepository.findByTaskId(taskId).ifPresentOrElse(taskRepository::delete, () -> {
            throw new ResourceNotFoundException("Task not found for ID: " + taskId);
        });
    }

    @Override
    public Long totalCount() {
        return taskRepository.count();
    }

}