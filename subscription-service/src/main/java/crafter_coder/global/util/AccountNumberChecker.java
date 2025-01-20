package crafter_coder.global.util;

import crafter_coder.client.AccountApiClient;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AccountNumberChecker {
    private final AccountApiClient accountApiClient;

    public boolean checkAccountNumber(String accountNumber) {
        try {
            accountApiClient.checkAccountNumber(accountNumber);
        } catch()
    }
}
