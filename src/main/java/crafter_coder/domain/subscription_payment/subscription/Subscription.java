package crafter_coder.domain.subscription_payment.subscription;

import crafter_coder.domain.subscription_payment.program.Program;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.*;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity @Getter
@NoArgsConstructor(access = PROTECTED)
public class Subscription {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Long debitAccountId;

    private String accountPassword;

    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumn(name = "program_id")
    private Program program;

    private Subscription(Long debitAccountId, String accountPassword, Program program) {
        this.debitAccountId = debitAccountId;
        this.accountPassword = accountPassword;
        this.program = program;
    }

    public static Subscription of(Long debitAccountId, String accountPassword, Program program) {

        return new Subscription(debitAccountId, accountPassword, program);
    }
}
