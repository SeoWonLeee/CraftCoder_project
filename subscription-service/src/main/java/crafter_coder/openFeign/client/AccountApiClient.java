package crafter_coder.openFeign.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(url = "http://13.209.87.161:8080/accounts")
public interface AccountApiClient {

    // 계좌 번호가 실제로 존재하는지 확인할 수 있도록 api가 필요해보이는데
    @GetMapping("/{accountNumber}")
    public Boolean checkAccountNumber(@RequestParam String accountNumber);
}
