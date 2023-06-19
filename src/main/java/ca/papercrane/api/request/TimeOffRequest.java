package ca.papercrane.api.request;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Represents an Employee's request for time off from work.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "time_off")
public final class TimeOffRequest {

    /**
     * The time off request id.
     */
    @Id
    @Column(name = "time_off_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer timeOffId;

    /**
     * The employeeId of the user that has requested the time off.
     */
    @Column(name = "employee_id", nullable = false)
    private Integer employeeId;

    /**
     * The date in which the employee's time off begins.
     */
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    /**
     * The date in which the time off request ends and the employee returns to work.
     */
    @Column(name = "end_date")
    private LocalDate endDate;

    /**
     * The current status of the request.
     */
    @Column(name = "status", length = 1, nullable = false)
    private char status;

    /**
     * The reason as to why the request was created.
     */
    @Column(name = "reason", length = 500, nullable = false)
    private String reason;

    /**
     * Creates a new TimeOffRequest
     *
     * @param employeeId The id of the employee requesting time off.
     * @param startDate  The date in which the time off will begin.
     * @param endDate    The date in which the employee will return to work.
     * @param reason     The brief reason as to why the time off has been requested.
     */
    public TimeOffRequest(Integer employeeId, LocalDate startDate, LocalDate endDate, String reason) {
        this.employeeId = employeeId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = 'O';
        this.reason = reason;
    }

}