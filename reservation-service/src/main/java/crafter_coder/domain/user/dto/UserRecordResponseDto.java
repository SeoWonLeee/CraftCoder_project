package crafter_coder.domain.user.dto;

import crafter_coder.domain.user.model.User;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class UserRecordResponseDto {
    private Long userId;
    private String username;
    private String name;
    private List<UserReservationDto> reservations;

    public static UserRecordResponseDto of(User user, List<UserReservationDto> reservations) {
        return UserRecordResponseDto.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .reservations(reservations)
                .build();
    }
}
