package crafter_coder.domain.subscription.model;

import crafter_coder.domain.program.model.Program;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumn(name = "program_id")
    @NotNull
    private Program program;

    @NotNull
    private String accountNumber;

    @NotNull
    private String accountPassword;

    private Subscription(Program program, String accountNumber, String accountPassword) {
        this.program = program;
        this.accountNumber = accountNumber;
        this.accountPassword = accountPassword;
    }

    public static Subscription of(Program program, String accountNumber, String accountPassword) {
        return new Subscription(program, accountNumber, accountPassword);
    }
}
