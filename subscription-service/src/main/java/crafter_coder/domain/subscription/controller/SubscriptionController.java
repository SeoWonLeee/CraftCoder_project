package crafter_coder.domain.subscription.controller;

import crafter_coder.domain.subscription.dto.SubscriptionReqDto;
import crafter_coder.domain.subscription.dto.SubscriptionResDto;
import crafter_coder.domain.subscription.service.SubscriptionService;
import crafter_coder.global.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/subscriptions")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @PostMapping("/subscribe")
    public ResponseEntity<ResponseDto<SubscriptionResDto>> subscribe(@RequestBody SubscriptionReqDto subscriptionReqDto) {
        SubscriptionResDto response = subscriptionService.subscribe(subscriptionReqDto);
        return ResponseEntity.ok(ResponseDto.success(response));
    }

    @DeleteMapping("/unsubscribe/{id}")
    public ResponseEntity<ResponseDto<SubscriptionResDto>> unsubscribe(@PathVariable Long id) {
        SubscriptionResDto response = subscriptionService.unsubscribe(id);
        return ResponseEntity.ok(ResponseDto.success(response));
    }
}
