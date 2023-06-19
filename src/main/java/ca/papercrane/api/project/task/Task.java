package ca.papercrane.api.project.task;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Represents a completable Task within a Project.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public final class Task {

    /**
     * The auto-generated task id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Integer taskId;

    /**
     * The id of the project that this task belongs to.
     */
    @Column(name = "project_id", nullable = false)
    private Integer projectId;

    /**
     * The name of the task.
     */
    @Column(name = "taskName", length = 25, nullable = false)
    private String taskName;

    /**
     * A brief description of what the task entails.
     */
    @Column(name = "description", length = 500, nullable = false)
    private String description;

    /**
     * The deadline date for when the task should/must be completed by.
     */
    @Column(name = "deadline", nullable = false)
    private LocalDate deadline;

    /**
     * The date that the task was/should be started on.
     */
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    /**
     * The date the tak was completed on.
     */
    @Column(name = "date_completed")
    private LocalDate dateCompleted;

    /**
     * The amount of hours that the task is expected to take to reach completion.
     */
    @Column(name = "expected_work_hours", nullable = false)
    private Double expectedWorkHours;

    /**
     * The current amount of hours dedicated to the task.
     */
    @Column(name = "progress_in_work_hours", columnDefinition = "double precision default 0")
    private Double progressInWorkHours = 0.0;

    /**
     * Represents if the task has been completed yet or not.
     */
    @Column(name = "is_complete")
    private boolean isComplete = false;

    /**
     * Creates a new Task.
     *
     * @param projectId         The id of the project this task is for.
     * @param description       A brief description of what the task is.
     * @param startDate         The date in which the task has been started.
     * @param deadline          The deadline for the task.
     * @param expectedWorkHours The amount of hours expected to complete the task.
     */
    public Task(Integer projectId, String taskName, String description, LocalDate deadline, LocalDate startDate, Double expectedWorkHours) {
        this.projectId = projectId;
        this.taskName = taskName;
        this.description = description;
        this.startDate = startDate;
        this.deadline = deadline;
        this.expectedWorkHours = expectedWorkHours;
        this.progressInWorkHours = 0.0;
        this.isComplete = false;
    }

}