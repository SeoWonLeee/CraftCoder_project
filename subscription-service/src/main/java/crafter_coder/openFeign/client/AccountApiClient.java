package crafter_coder.openFeign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(url = "***")
public interface AccountApiClient {
    // 아직 1팀에서 정확히 뭐를 반환해줄지 모르겠어서 일단 Boolean으로 했습니다.
    @GetMapping("/accounts/{accountNumber}")
    public Boolean checkAccountNumber(@RequestParam String accountNumber);
}
