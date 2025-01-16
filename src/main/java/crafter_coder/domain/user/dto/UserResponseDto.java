package crafter_coder.domain.user.dto;

import crafter_coder.domain.user.model.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDto {
    private Long id;
    private String username;
    private String name;
    private String phoneNumber;
    private String birthDate;
    private String status;
    private String role;

    public static UserResponseDto of(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .birthDate(user.getBirthDate().toString())
                .status(user.getStatus().name())
                .role(user.getRole().name())
                .build();
    }
}
