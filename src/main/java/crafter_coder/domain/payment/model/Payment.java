package crafter_coder.domain.payment.model;

import crafter_coder.domain.user.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private int amount;

    @Column(nullable = false)
    private PaymentStatus paymentStatus;

    @ManyToOne(fetch = LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Payment(int amount, PaymentStatus paymentStatus) {
        this.amount = amount;
        this.paymentStatus = paymentStatus;
    }

    public static Payment of(int amount, PaymentStatus paymentStatus) {
        return new Payment(amount, paymentStatus);
    }
}
