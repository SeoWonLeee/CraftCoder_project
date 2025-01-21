package crafter_coder.domain.program.model;

import crafter_coder.domain.subscription.model.Subscription;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Program {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private BigDecimal price;

    // 결제일(ex. 1~31일 중 하나)
    @NotNull
    private int billingDate;

    @NotNull
    private String accountNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ProgramStatus status;

    @OneToMany(mappedBy = "program", fetch = FetchType.LAZY)
    private List<Subscription> subscriptions = new ArrayList<>();

    @Builder
    private Program(String name, BigDecimal price, int billingDate, String accountNumber, ProgramStatus status) {
        this.name = name;
        this.price = price;
        this.billingDate = billingDate;
        this.accountNumber = accountNumber;
        this.status = status;
    }

    public static Program of(String name, BigDecimal price, int billingDate, String accountNumber, ProgramStatus status) {
        return Program.builder()
                .name(name)
                .price(price)
                .billingDate(billingDate)
                .accountNumber(accountNumber)
                .status(status)
                .build();
    }

    public void softDelete() {
        this.status = ProgramStatus.DELETED;
    }

    public void updateStatus(ProgramStatus status) {
        this.status = status;
    }
}
