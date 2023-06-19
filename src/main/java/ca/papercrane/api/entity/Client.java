package ca.papercrane.api.entity;

import ca.papercrane.api.entity.role.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Represents a Client user.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Entity
@Table(name = "client")
@PrimaryKeyJoinColumn(name = "user_id")
public final class Client extends User {

    /**
     * The name of the client.
     */
    @Column(name = "client_name", length = 50, nullable = false)
    private String clientName;

    /**
     * The name of the client's company.
     */
    @Column(name = "company_name", length = 50, nullable = false)
    private String companyName;

    /**
     * Creates a new Client.
     *
     * @param email       The email for the user.
     * @param password    The password for the user.
     * @param clientName  The clients name.
     * @param companyName The clients company name.
     */
    public Client(String email, String password, String clientName, String companyName) {
        super(email, password, UserRole.CLIENT);
        this.clientName = clientName;
        this.companyName = companyName;
    }

}