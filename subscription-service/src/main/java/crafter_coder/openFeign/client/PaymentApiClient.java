package crafter_coder.openFeign.client;

import crafter_coder.domain.payment.dto.PaymentResDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(url = "***")
public interface PaymentApiClient {
    @PostMapping("/payments")
    PaymentResDto requestPayment();
}
