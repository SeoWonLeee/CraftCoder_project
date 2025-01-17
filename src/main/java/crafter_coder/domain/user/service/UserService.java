package crafter_coder.domain.user.service;

import crafter_coder.domain.user.dto.LoginRequestDto;
import crafter_coder.domain.user.dto.RegisterRequestDto;
import crafter_coder.domain.user.dto.UserResponseDto;
import crafter_coder.domain.user.mapper.UserMapper;
import crafter_coder.domain.user.model.User;
import crafter_coder.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    //회원가입
    public UserResponseDto register(RegisterRequestDto registerRequestDto) {
        //중복 사용자 확인
        validatorUserDoesNotExist(registerRequestDto);

        //비밀번호 암호화
        String encryptedPassword = passwordEncoder.encode(registerRequestDto.getPassword());

        // User 객체 생성 및 초기 비밀번호 설정
        User user = User.of(
                registerRequestDto.getUsername(),
                encryptedPassword,
                registerRequestDto.getName(),
                registerRequestDto.getPhoneNumber(),
                registerRequestDto.getEmail(),
                registerRequestDto.getBirthDate(),
                registerRequestDto.getAccountId(),
                registerRequestDto.getAccountPassword(),
                registerRequestDto.getRole(),
                registerRequestDto.getStatus()
        );

        //사용자 저장
        userRepository.save(user);
        //응답 dto
        return userMapper.toDto(user);
    }

    //로그인
    public UserResponseDto login(LoginRequestDto loginRequestDto) {
        User user = findUserByUsernameOrThrow(loginRequestDto);

        validatorPasswordOrThrow(loginRequestDto, user);

        return userMapper.toDto(user);
    }

    private void validatorUserDoesNotExist(RegisterRequestDto registerRequestDto) {
        if (userRepository.findByUsername(registerRequestDto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("User with this username already exists.");
        }
    }


    private User findUserByUsernameOrThrow(LoginRequestDto loginRequestDto) {
        return userRepository.findByUsername(loginRequestDto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password."));
    }

    private void validatorPasswordOrThrow(LoginRequestDto loginRequestDto, User user) {
        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid password.");
        }
    }
}
