package org.example.individualbackend.domain.create;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {
    @NotBlank
    private String email;
    @NotBlank
    private String fName;
    @NotBlank
    private String lName;
    @NotBlank
    private String picture;
    @NotBlank
    private String password;
    @NotBlank
    private String role;
}
