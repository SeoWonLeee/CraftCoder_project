package crafter_coder.domain.payment.model;

import crafter_coder.domain.subscription.model.Subscription;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Payment {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private PaymentStatus paymentStatus;

    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumn(name = "subscription_id", nullable = false)
    private Subscription subscription;

    private Payment(BigDecimal amount, PaymentStatus paymentStatus) {
        this.amount = amount;
        this.paymentStatus = paymentStatus;
    }

    public static Payment of(BigDecimal amount, PaymentStatus paymentStatus) {
        return new Payment(amount, paymentStatus);
    }
}
