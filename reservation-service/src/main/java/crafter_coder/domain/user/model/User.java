package crafter_coder.domain.user.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = IDENTITY) // IDENTITY: Auto-increment 사용
    private Long id;

    @Column(nullable = false, unique = true, updatable = false) //user 지정 아이디
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(name = "account_id", nullable = true)
    private String accountId; // 계좌 ID

    @Column(name = "account_password", nullable = false)
    private String accountPassword; // 계좌 비밀번호

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActiveStatus status;

    private User(String username, String password, String name, String phoneNumber, String email, LocalDate birthDate,
                 String accountId, String accountPassword, Role role, ActiveStatus status) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.birthDate = birthDate;
        this.accountId = accountId;
        this.accountPassword = accountPassword;
        this.role = role;
        this.status = status;
    }

    public static User of(String username, String password, String name, String phoneNumber, String email, LocalDate birthDate,
                          String accountId, String accountPassword, Role role, ActiveStatus status) {
        return new User(username, password, name, phoneNumber, email, birthDate, accountId, accountPassword, role, status);
    }

    public void updateStatus(ActiveStatus newStatus) {
        this.status = newStatus;
    }

    public void updateName(String name) { this.name = name; }

    public void updatePhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public void updateEmail(String email) { this.email = email; }

    public void updateBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public void updateAccountId(String accountId) { this.accountId = accountId; }

    public void updateAccountPassword(String accountPassword) { this.accountPassword = accountPassword; }

    public void updateRole(Role role) { this.role = role; }

    public void updatePassword(String updatePassword) { this.password = updatePassword; }
}
