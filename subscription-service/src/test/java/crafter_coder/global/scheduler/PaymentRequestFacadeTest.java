package crafter_coder.global.scheduler;

import crafter_coder.domain.payment.model.Payment;
import crafter_coder.domain.payment.repository.PaymentRepository;
import crafter_coder.domain.program.model.Program;
import crafter_coder.domain.program.model.ProgramStatus;
import crafter_coder.domain.program.repository.ProgramRepository;
import crafter_coder.domain.subscription.model.Subscription;
import crafter_coder.domain.subscription.model.SubscriptionStatus;
import crafter_coder.domain.subscription.repository.SubscriptionRepository;
import crafter_coder.global.exception.RestApiException;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
class PaymentRequestFacadeTest {

    @Autowired
    private PaymentRequestFacade paymentRequestFacade;

    @Autowired
    private ProgramRepository programRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Test
    @DisplayName("결제일에 해당하는 프로그램을 구독 중인 사용자에게 결제 요청을 보낸다.")
    @Transactional
    void processProgramPayments() {
        // given
        int dayOfMonth = 15;
        Program program = Program.of("프로그램", BigDecimal.valueOf(1000), dayOfMonth,"8591138-891036980" ,ProgramStatus.ACTIVE);
        Subscription subscription = Subscription.of(program, "0550776-465073841", "9iTf>O7V8gpxXVTq3", SubscriptionStatus.ACTIVE);
        program.getSubscriptions().add(subscription);

        programRepository.save(program);
        subscriptionRepository.save(subscription);

        // when
        paymentRequestFacade.processProgramPayments(dayOfMonth);

        // then
        // 실제 결제 요청이 성공적으로 처리되었는지 검증
        List<Payment> payments = paymentRepository.findAll();
        assertThat(payments).hasSize(1);
        assertThat(payments.get(0).getAmount()).isEqualTo(program.getPrice());
    }

    @Test
    @DisplayName("계좌번호가 존재하지 않을 경우 결제 요청에서 예외가 발생한다.")
    @Transactional
    void processProgramPayments_inValidAccountNumber() {
        // given
        int dayOfMonth = 15;
        Program program = Program.of("프로그램", BigDecimal.valueOf(15000), dayOfMonth,"00000000000" ,ProgramStatus.ACTIVE);
        Subscription subscription = Subscription.of(program, "00000000000", "password", SubscriptionStatus.ACTIVE);
        program.getSubscriptions().add(subscription);

        programRepository.save(program);
        subscriptionRepository.save(subscription);

        // when, then
        assertThatThrownBy(() -> paymentRequestFacade.processProgramPayments(dayOfMonth))
                .isInstanceOf(RestApiException.class)
                .hasMessageContaining("존재하지 않는 계좌입니다.");

    }
}