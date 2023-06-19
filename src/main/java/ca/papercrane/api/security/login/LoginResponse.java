package ca.papercrane.api.security.login;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public final class LoginResponse {

    private Integer id;
    private String email;
    private String role;
    private String token;

}