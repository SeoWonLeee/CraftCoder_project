package crafter_coder.domain.subscription.controller;

import crafter_coder.domain.subscription.dto.SubscriptionReqDto;
import crafter_coder.domain.subscription.dto.SubscriptionResDto;
import crafter_coder.domain.subscription.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/subscriptions")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @PostMapping("/subscribe")
    public ResponseEntity<SubscriptionResDto> subscribe(@RequestBody SubscriptionReqDto subscriptionReqDto) {
        SubscriptionResDto subscriptionResDto = subscriptionService.subscribe(subscriptionReqDto);
        return ResponseEntity.ok(subscriptionResDto);
    }

    @DeleteMapping("/unsubscribe/{id}")
    public ResponseEntity<SubscriptionResDto> unsubscribe(@PathVariable Long id) {
        return ResponseEntity.ok(subscriptionService.unsubscribe(id));
    }
}
