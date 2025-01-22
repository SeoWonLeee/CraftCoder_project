package crafter_coder.global.scheduler;

import crafter_coder.domain.payment.dto.PaymentReqDto;
import crafter_coder.domain.payment.dto.PaymentResDto;
import crafter_coder.domain.payment.model.Payment;
import crafter_coder.domain.payment.repository.PaymentRepository;
import crafter_coder.domain.program.model.Program;
import crafter_coder.domain.program.model.ProgramStatus;
import crafter_coder.domain.program.repository.ProgramRepository;
import crafter_coder.domain.subscription.model.Subscription;
import crafter_coder.domain.subscription.model.SubscriptionStatus;
import crafter_coder.domain.subscription.repository.SubscriptionRepository;
import crafter_coder.global.openFeign.client.PaymentApiClient;
import crafter_coder.global.openFeign.exception.RestApiException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class PaymentRequestFacadeIntegrationTest {

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
    void processProgramPayments() {
        // given
        int dayOfMonth = 15;
        Program program = Program.of("프로그램", BigDecimal.valueOf(15000), dayOfMonth,"123412341234" ,ProgramStatus.ACTIVE);
        Subscription subscription = Subscription.of(program, "9876543210", "password", SubscriptionStatus.ACTIVE);

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
    void processProgramPayments_inValidAccountNumber() {
        // given
        int dayOfMonth = 15;
        Program program = Program.of("프로그램", BigDecimal.valueOf(15000), dayOfMonth,"00000000000" ,ProgramStatus.ACTIVE);
        Subscription subscription = Subscription.of(program, "00000000000", "password", SubscriptionStatus.ACTIVE);

        programRepository.save(program);
        subscriptionRepository.save(subscription);

        // when, then
        assertThatThrownBy(() -> paymentRequestFacade.processProgramPayments(dayOfMonth))
                .isInstanceOf(RestApiException.class)
                .hasMessageContaining("존재하지 않는 계좌입니다.");

    }
}