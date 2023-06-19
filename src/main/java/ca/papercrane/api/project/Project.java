package ca.papercrane.api.project;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a Project within the application.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "project")
public final class Project {

    /**
     * The auto-generated project id.
     */
    @Id
    @Column(name = "project_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer projectId;

    /**
     * The id of the client user that this project is for.
     */
    @Column(name = "client_id", nullable = false)
    private Integer clientId;

    /**
     * The user id of the project leader.
     */
    @Column(name = "project_lead_id", nullable = false)
    private Integer projectLeadId;

    /**
     * A brief description of what the project is.
     */
    @Column(name = "project_description")
    private String projectDescription;

    /**
     * Creates a new Project.
     *
     * @param clientId           The id of the client user that this project is for.
     * @param projectLeadId      The id of the project leader.
     * @param projectDescription The brief description of the project.
     */
    public Project(Integer clientId, Integer projectLeadId, String projectDescription) {
        this.clientId = clientId;
        this.projectLeadId = projectLeadId;
        this.projectDescription = projectDescription;
    }

}