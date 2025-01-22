package crafter_coder.global.redis;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AddQueueResponse {
    private String message;

    private AddQueueResponse(String message) {
        this.message = message;
    }

    public static AddQueueResponse of(String message) {
        return new AddQueueResponse(message);
    }
}
