package crafter_coder.domain.subscription.exception;

import crafter_coder.global.exception.MyErrorCode;
import lombok.Getter;

@Getter
public class SubscriptionException extends RuntimeException {
    private final MyErrorCode myErrorCode;

    public SubscriptionException(MyErrorCode myErrorCode) {
        super(myErrorCode.getMessage());
        this.myErrorCode = myErrorCode;
    }

    public static class SubscriptionNotFoundException extends SubscriptionException {
        public SubscriptionNotFoundException() {
            super(MyErrorCode.SUBSCRIPTION_NOT_FOUND);
        }
    }
}
