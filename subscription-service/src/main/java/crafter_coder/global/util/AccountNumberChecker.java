package crafter_coder.global.util;

import crafter_coder.openFeign.client.AccountApiClient;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AccountNumberChecker {
    private final AccountApiClient accountApiClient;

    public boolean checkAccountNumber(String accountNumber) {
        return accountApiClient.checkAccountNumber(accountNumber);
    }
}
