package crafter_coder.domain.instructor.dto;

import crafter_coder.domain.user.model.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InstructorResponseDto {
    private Long id;
    private String name;
    private String phoneNumber;
    private String specialization;

    public static InstructorResponseDto of(User instructor) {
        return InstructorResponseDto.builder()
                .id(instructor.getId())
                .name(instructor.getName())
                .phoneNumber(instructor.getPhoneNumber())
                .specialization(instructor.getSpecialization())
                .build();
    }
}
