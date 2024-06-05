package org.example.individualbackend.domain.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9_-]+\\.[A-Za-z]{2,}$")
    private String email;
    @NotBlank
    private String fName;
    @NotBlank
    private String lName;
    @NotBlank
    private String picture;
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&_])[A-Za-z\\d@$!%*?&_]{12,}$")
    private String password;
    @NotBlank
    private String role;
}
