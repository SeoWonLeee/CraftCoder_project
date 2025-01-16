package crafter_coder.domain.user.service;

import crafter_coder.domain.user.dto.UpdatePasswordRequestDto;
import crafter_coder.domain.user.dto.UpdateProfileRequestDto;
import crafter_coder.domain.user.dto.UserResponseDto;
import crafter_coder.domain.user.mapper.UserMapper;
import crafter_coder.domain.user.model.ActiveStatus;
import crafter_coder.domain.user.model.User;
import crafter_coder.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    //회원 프로필 수정
    public UserResponseDto updateProfile(String username, UpdateProfileRequestDto updateProfileRequestDto){
        User user = findUserByUsernameOrThrow(username);

        UpdateUserProfilelds(updateProfileRequestDto, user);

        userRepository.save(user);
        return userMapper.toDto(user);
    }

    //회원 비밀면호 수정
    public void UpdateUserPassword(String username, UpdatePasswordRequestDto updatePasswordRequestDto){
        User user = findUserByUsernameOrThrow(username);

        validateCurrentPassword(updatePasswordRequestDto, user);

        updateUserPassword(updatePasswordRequestDto, user);
        userRepository.save(user);
    }

    //회원탈퇴
    public void deactivateUser(String username) {
        User user = findUserByUsernameOrThrow(username);
        user.updateStatus(ActiveStatus.DEACTIVED);
        userRepository.save(user);
    }

    private User findUserByUsernameOrThrow(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    private void validateCurrentPassword(UpdatePasswordRequestDto updatePasswordRequestDto, User user) {
        if(!passwordEncoder.matches(updatePasswordRequestDto.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }
    }

    private void updateUserPassword(UpdatePasswordRequestDto updatePasswordRequestDto, User user) {
        String encryptedPassword = passwordEncoder.encode(updatePasswordRequestDto.getNewPassword());
        user.updatePassword(encryptedPassword);
    }

    private void UpdateUserProfilelds(UpdateProfileRequestDto updateProfileRequestDto, User user) {
        if (updateProfileRequestDto.getName() != null)
            user.updateName(updateProfileRequestDto.getName());
        if (updateProfileRequestDto.getPhoneNumber() != null)
            user.updatePhoneNumber(updateProfileRequestDto.getPhoneNumber());
        if (updateProfileRequestDto.getEmail() != null)
            user.updateEmail(updateProfileRequestDto.getEmail());
        if (updateProfileRequestDto.getBirthDate() != null)
            user.updateBirthDate(updateProfileRequestDto.getBirthDate());
        if (updateProfileRequestDto.getAccountId() != null)
            user.updateAccountId(updateProfileRequestDto.getAccountId());
        if(updateProfileRequestDto.getAccountPassword() != null)
            user.updateAccountPassword(updateProfileRequestDto.getAccountPassword());
        if (updateProfileRequestDto.getRole() != null)
            user.updateRole(updateProfileRequestDto.getRole());
        if (updateProfileRequestDto.getStatus() != null)
            user.updateStatus(updateProfileRequestDto.getStatus());
    }
}
