package crafter_coder.domain.subscription.model;

import crafter_coder.domain.program.model.Program;
import crafter_coder.domain.program.model.ProgramStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumn(name = "program_id", nullable = false)
    private Program program;

    @NotNull
    private String accountNumber;

    @NotNull
    private String accountPassword;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SubscriptionStatus status;

    @Builder(access = AccessLevel.PRIVATE)
    private Subscription(Program program, String accountNumber, String accountPassword, SubscriptionStatus status) {
        this.program = program;
        this.accountNumber = accountNumber;
        this.accountPassword = accountPassword;
        this.status = status;
    }

    public static Subscription of(Program program, String accountNumber, String accountPassword, SubscriptionStatus status) {
        return Subscription.builder()
                .program(program)
                .accountNumber(accountNumber)
                .accountPassword(accountPassword)
                .status(status)
                .build();
    }

    public void softDelete() {
        this.status = SubscriptionStatus.DELETED;
    }

    public void updateStatus(SubscriptionStatus status) {
        this.status = status;
    }

    public boolean isActive() {
        return this.status == SubscriptionStatus.ACTIVE;
    }
}
