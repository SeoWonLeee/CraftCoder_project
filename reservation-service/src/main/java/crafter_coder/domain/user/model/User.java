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
    @GeneratedValue(strategy = IDENTITY)
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
    private LocalDate birthDate;

    @Column(name = "account_id", nullable = false)
    private String accountId;

    @Column(name = "account_password", nullable = false)
    private String accountPassword;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActiveStatus status;

    @Column(nullable = true)
    private String specialization;

    private User(String username, String password, String name, String phoneNumber, LocalDate birthDate,
                 String accountId, String accountPassword, Role role, ActiveStatus status, String specialization) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.accountId = accountId;
        this.accountPassword = accountPassword;
        this.role = role;
        this.status = status;
        this.specialization = specialization;
    }

    public static User of(String username, String password, String name, String phoneNumber, LocalDate birthDate,
                          String accountId, String accountPassword, Role role, ActiveStatus status, String specialization) {
        return new User(username, password, name, phoneNumber, birthDate, accountId, accountPassword, role, status, specialization);
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updatePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void updateBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void updateAccountId(String accountId) {
        this.accountId = accountId;
    }

    public void updateAccountPassword(String accountPassword) {
        this.accountPassword = accountPassword;
    }

    public void updateRole(Role role) {
        this.role = role;
    }

    public void updateStatus(ActiveStatus newStatus) {
        this.status = newStatus;
    }
}