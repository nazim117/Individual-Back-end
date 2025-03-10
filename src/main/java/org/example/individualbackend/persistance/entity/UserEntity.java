package org.example.individualbackend.persistance.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

@Entity
@Table(name="users")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotBlank
    @Length(min = 5, max = 50)
    @Column(name = "email")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9_-]+\\.[A-Za-z]{2,}$")
    private String email;

    @NotBlank
    @Length(min = 2, max = 50)
    @Column(name = "f_name")
    private String fName;

    @NotBlank
    @Length(min = 2, max = 50)
    @Column(name = "l_name")
    private String lName;

    @NotBlank
    @Length(min = 2, max = 255)
    @Column(name = "password")
    private String password;

    @OneToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "fan_id")
    private FanEntity fan;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private Set<UserRoleEntity> userRoles;
}
