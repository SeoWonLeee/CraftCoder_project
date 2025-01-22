package crafter_coder.domain.subscription.service;

import crafter_coder.domain.program.exception.ProgramException;
import crafter_coder.domain.program.model.Program;
import crafter_coder.domain.program.repository.ProgramRepository;
import crafter_coder.domain.subscription.dto.SubscriptionReqDto;
import crafter_coder.domain.subscription.dto.SubscriptionResDto;
import crafter_coder.domain.subscription.exception.SubscriptionException;
import crafter_coder.domain.subscription.exception.SubscriptionException.SubscriptionNotFoundException;
import crafter_coder.domain.subscription.model.Subscription;
import crafter_coder.domain.subscription.model.SubscriptionStatus;
import crafter_coder.domain.subscription.repository.SubscriptionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final ProgramRepository programRepository;


    @Transactional
    public SubscriptionResDto subscribe(SubscriptionReqDto subscriptionReqDto) {
        // TODO: 계좌번호와 비밀번호 해당하는 계좌가 존재하는지, 이미 구독중인 지(계좌번호가 동일한게 등록되어 있는지) 체크 필요
        Long programId = subscriptionReqDto.programId();
        Program foundProgram = findProgramById(programId);
        Subscription subscription = Subscription.of(
                foundProgram,
                subscriptionReqDto.accountNumber(),
                subscriptionReqDto.accountPassword(),
                SubscriptionStatus.ACTIVE);

        subscriptionRepository.save(subscription);

        return SubscriptionResDto.of(subscription.getId());
    }

    private Program findProgramById(Long programId) {
        return programRepository.findById(programId)
                .orElseThrow(ProgramException.ProgramNotFoundException::new);
    }

    @Transactional
    public SubscriptionResDto unsubscribe(Long subscriptionId) {
        Subscription subscription = findSubscriptionById(subscriptionId);
        subscription.softDelete();
        return SubscriptionResDto.of(subscription.getId());
    }

    private Subscription findSubscriptionById(Long subscriptionId) {
        return subscriptionRepository.findById(subscriptionId)
                .orElseThrow(SubscriptionNotFoundException::new);
    }
}
