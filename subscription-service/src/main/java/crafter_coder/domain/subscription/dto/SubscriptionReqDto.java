package crafter_coder.domain.subscription.dto;

import crafter_coder.domain.subscription.model.Subscription;

public record SubscriptionReqDto(
        String accountNumber,
        String accountPassword,
        Long programId
) {
}
