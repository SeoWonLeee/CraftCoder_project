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
        Long programId = subscriptionReqDto.programId();
        Program foundProgram = findProgramById(programId);
        Subscription subscription = Subscription.of(foundProgram, subscriptionReqDto.accountNumber(), subscriptionReqDto.accountPassword());
        subscriptionRepository.save(subscription);

        return SubscriptionResDto.of(subscription.getId());
    }

    private Program findProgramById(Long programId) {
        return programRepository.findById(programId)
                .orElseThrow(ProgramException.ProgramNotFoundException::new);
    }

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
