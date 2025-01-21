package crafter_coder.domain.user.service;

import crafter_coder.domain.user.model.Role;
import crafter_coder.domain.user.model.User;
import crafter_coder.domain.user.repository.UserRepository;
import crafter_coder.global.exception.MyErrorCode;
import crafter_coder.global.exception.MyException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InstructorService {

    private final UserRepository userRepository;

    public boolean isValidInstructor(Long instructorId) {
        return userRepository.findById(instructorId)
                .filter(user -> user.getRole() == Role.INSTRUCTIOR)
                .isPresent();
    }

    public User getInstructor(Long instructorId) {
        return userRepository.findById(instructorId)
                .filter(user -> user.getRole() == Role.INSTRUCTIOR)
                .orElseThrow(() -> new MyException(MyErrorCode.INSTRUCTOR_ONLY));
    }
}
