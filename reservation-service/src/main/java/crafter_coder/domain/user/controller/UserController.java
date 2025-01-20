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

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@Valid @RequestBody RegisterRequestDto registerRequestDto) {
        UserResponseDto response = userService.register(registerRequestDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        UserResponseDto response = userService.login(loginRequestDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return ResponseEntity.ok("Logged out successfully.");
    }

    @PutMapping("/update/profile/{username}")
    public ResponseEntity<UserResponseDto> updateProfile(@PathVariable String username,
                                                         @Valid @RequestBody UpdateProfileRequestDto updateProfileRequestDto) {
        UserResponseDto response = userProfileService.updateProfile(username, updateProfileRequestDto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/update/password/{username}")
    public ResponseEntity<String> updatePassword(@PathVariable String username,
                                                 @Valid @RequestBody UpdatePasswordRequestDto updatePasswordRequestDto) {
        userProfileService.UpdateUserPassword(username, updatePasswordRequestDto);
        return ResponseEntity.ok("Password updated successfully.");
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<String> deactivateUser(@PathVariable String username) {
        userProfileService.deactivateUser(username);
        return ResponseEntity.ok("User deactivated successfully.");
    }

}
