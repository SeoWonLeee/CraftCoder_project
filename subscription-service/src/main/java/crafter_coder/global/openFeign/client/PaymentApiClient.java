package crafter_coder.global.openFeign.client;

import crafter_coder.domain.payment.dto.PaymentReqDto;
import crafter_coder.domain.payment.dto.PaymentResDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "paymentApiClient", url = "http://13.209.87.161:8080/payment")
public interface PaymentApiClient {
    @PostMapping
    PaymentResDto requestPayment(@RequestBody PaymentReqDto paymentReqDto);
}
