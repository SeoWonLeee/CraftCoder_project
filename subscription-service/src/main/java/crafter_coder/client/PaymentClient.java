package crafter_coder.client;

import crafter_coder.domain.payment.dto.PaymentResDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "payment-service", url = "***")
public interface PaymentClient {
    @PostMapping("/api/v1/payments")
    PaymentResDto requestPayment();
}
