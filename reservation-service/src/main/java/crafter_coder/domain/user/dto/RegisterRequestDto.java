package crafter_coder.domain.user.dto;

import crafter_coder.domain.user.model.ActiveStatus;
import crafter_coder.domain.user.model.Role;
import crafter_coder.domain.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDto {
    private String username;
    private String password;
    private String name;
    private String phoneNumber;
    private String email;
    private LocalDate birthDate;
    private String bank;
    private String accountId;
    private String accountPassword;
    private Role role;
    private ActiveStatus status;

    public User toEntity() {
        return User.of(
                username,
                password,
                name,
                phoneNumber,
                email,
                birthDate,
                accountId,
                accountPassword,
                role,
                status
        );
    }

}
