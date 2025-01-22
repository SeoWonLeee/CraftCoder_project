package crafter_coder.global.openFeign.client;

import crafter_coder.global.openFeign.dto.PaymentReqDto;
import crafter_coder.global.openFeign.dto.PaymentResDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "paymentApiClient", url = "http://13.209.87.161:8080/payment")
public interface PaymentApiClient {
    @PostMapping
    PaymentResDto requestPayment(@Valid @RequestBody PaymentReqDto paymentReqDto);
}
