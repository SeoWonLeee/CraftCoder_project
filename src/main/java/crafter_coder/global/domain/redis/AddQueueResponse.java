package crafter_coder.global.domain.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

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
