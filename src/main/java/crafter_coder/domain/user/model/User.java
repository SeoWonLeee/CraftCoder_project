package crafter_coder.domain.user.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = IDENTITY) // IDENTITY: Auto-increment 사용
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private LocalDateTime birthDate;

    @Column(name = "account_id", nullable = false)
    private String accountId; // 계좌 ID

    @Column(name = "account_password", nullable = false)
    private String accountPassword; // 계좌 비밀번호

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActiveStatus status;

    private User(String username, String password, String name, String phoneNumber, LocalDateTime birthDate,
                 String accountId, String accountPassword, Role role, ActiveStatus status) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.accountId = accountId;
        this.accountPassword = accountPassword;
        this.role = role;
        this.status = status;
    }

    public static User of(String username, String password, String name, String phoneNumber, LocalDateTime birthDate,
                          String accountId, String accountPassword, Role role, ActiveStatus status) {
        return new User(username, password, name, phoneNumber, birthDate, accountId, accountPassword, role, status);
    }

}
