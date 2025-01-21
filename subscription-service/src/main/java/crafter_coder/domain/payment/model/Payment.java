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
    private Long id;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private PaymentStatus paymentStatus;

    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumn(name = "subscription_id", nullable = false)
    private Subscription subscription;

    private Payment(Long id, BigDecimal amount, PaymentStatus paymentStatus, Subscription subscription) {
        this.id = id;
        this.amount = amount;
        this.paymentStatus = paymentStatus;
        this.subscription = subscription;
    }

    public static Payment of(Long id, BigDecimal amount, PaymentStatus paymentStatus, Subscription subscription) {
        return new Payment(id, amount, paymentStatus, subscription);
    }
}
