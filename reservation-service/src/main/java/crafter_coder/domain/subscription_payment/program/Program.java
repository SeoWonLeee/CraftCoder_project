package crafter_coder.domain.subscription_payment.program;

import crafter_coder.global.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.*;

@Entity @Getter
@NoArgsConstructor(access = PROTECTED)
public class Program extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private int price;

    private LocalDateTime billingDate;

    private String name;

    private String description;

    private Long creditAccountId;

    private Program(int price, LocalDateTime billingDate, String name, String description, Long creditAccountId) {
        this.price = price;
        this.billingDate = billingDate;
        this.name = name;
        this.description = description;
        this.creditAccountId = creditAccountId;
    }

    public static Program of(int price, LocalDateTime billingDate, String name, String description, Long creditAccountId){
        return new Program(price, billingDate, name, description, creditAccountId);
    }

}
