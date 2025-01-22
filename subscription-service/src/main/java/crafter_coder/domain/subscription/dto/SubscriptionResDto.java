package crafter_coder.domain.subscription.dto;

public record SubscriptionResDto(
        Long subscriptionId
) {
    public static SubscriptionResDto of(Long subscriptionId) {
        return new SubscriptionResDto(subscriptionId);
    }
}
