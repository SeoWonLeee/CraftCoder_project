package crafter_coder.domain.user.mapper;

import crafter_coder.domain.user.dto.UserResponseDto;
import crafter_coder.domain.user.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponseDto toDto(User user){
        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .birthDate(user.getBirthDate())
                .accountId(user.getAccountId())
                .accountPassword(user.getAccountPassword())
                .role(user.getRole())
                .status(user.getStatus())
                .build();

    }
}
