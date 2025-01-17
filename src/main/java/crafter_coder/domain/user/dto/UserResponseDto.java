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
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {
    private Long id;
    private String username;
    private String name;
    private String phoneNumber;
    private String email;
    private LocalDate birthDate;
    private String accountId;
    private String accountPassword;
    private Role role;
    private ActiveStatus status;


    public static UserResponseDto of(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .birthDate(LocalDate.parse(user.getBirthDate().toString()))
                .role(Role.valueOf(user.getRole().name()))
                .status(ActiveStatus.valueOf(user.getStatus().name()))
                .build();
    }
}
