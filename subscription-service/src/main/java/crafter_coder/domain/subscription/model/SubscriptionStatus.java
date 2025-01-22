package crafter_coder.domain.subscription.model;

public enum SubscriptionStatus {
    ACTIVE,
    INACTIVE, // 결제일에 결제 실패 시 INACTIVE로 전환
    DELETED
}
