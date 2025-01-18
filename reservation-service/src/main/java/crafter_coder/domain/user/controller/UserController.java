package crafter_coder.domain.user.controller;

import crafter_coder.domain.user.dto.*;
import crafter_coder.domain.user.model.User;
import crafter_coder.domain.user.service.BlacklistService;
import crafter_coder.domain.user.service.UserProfileService;
import crafter_coder.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserProfileService userProfileService;
    private final BlacklistService blacklistService;

    //회원가입
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@Valid @RequestBody RegisterRequestDto registerRequestDto) {
        UserResponseDto response = userService.register(registerRequestDto);
        return ResponseEntity.ok(response);
    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        UserResponseDto response = userService.login(loginRequestDto);
        return ResponseEntity.ok(response);
    }

    //로그아웃
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        // 세션 무효화
        request.getSession().invalidate();
        return ResponseEntity.ok("Logged out successfully.");
    }

    //회원정보수정
    @PutMapping("/update/profile/{username}")
    public ResponseEntity<UserResponseDto> updateProfile(@PathVariable String username,
                                                         @Valid @RequestBody UpdateProfileRequestDto updateProfileRequestDto) {
        UserResponseDto response = userProfileService.updateProfile(username, updateProfileRequestDto);
        return ResponseEntity.ok(response);
    }

    //비밀번호 변경
    @PatchMapping("/update/password/{username}")
    public ResponseEntity<String> updatePassword(@PathVariable String username,
                                                 @Valid @RequestBody UpdatePasswordRequestDto updatePasswordRequestDto) {
        userProfileService.UpdateUserPassword(username, updatePasswordRequestDto);
        return ResponseEntity.ok("Password updated successfully.");
    }

    // 회원 탈퇴
    @DeleteMapping("/{username}")
    public ResponseEntity<String> deactivateUser(@PathVariable String username) {
        userProfileService.deactivateUser(username);
        return ResponseEntity.ok("User deactivated successfully.");
    }

}
