package crafter_coder.domain.subscription.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class SubscriptionException extends RuntimeException {
    private final SubscriptionErrorCode errorCode;

    public SubscriptionException(SubscriptionErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public static class SubscriptionNotFoundException extends SubscriptionException {
        public SubscriptionNotFoundException() {
            super(SubscriptionErrorCode.SUBSCRIPTION_NOT_FOUND);
        }
    }
}
