package org.example.individualbackend.domain.update;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {
    private Integer id;
    @NotBlank
    private String fName;
    @NotBlank
    private String lName;
    @NotBlank
    private String password;
    @NotBlank
    private String picture;
}
