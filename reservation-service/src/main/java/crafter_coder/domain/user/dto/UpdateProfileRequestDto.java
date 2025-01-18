package crafter_coder.domain.user.dto;

import crafter_coder.domain.user.model.ActiveStatus;
import crafter_coder.domain.user.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileRequestDto {
    private String name;
    private String phoneNumber;
    private String email;
    private LocalDate birthDate;
    private String accountId;
    private String accountPassword;
    private Role role;
    private ActiveStatus status;
}
