package io.groom.scubadive.shoppingmall.member.util;

import io.groom.scubadive.shoppingmall.member.domain.User;
import io.groom.scubadive.shoppingmall.member.domain.enums.UserStatus;
import io.groom.scubadive.shoppingmall.member.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserDormancyScheduler {

    private final UserRepository userRepository;

    @Transactional
    @Scheduled(cron = "0 0 1 * * *")
//    @Scheduled(cron = "0 * * * * *")
    public void markInactiveUsersAsDormant() {
        LocalDateTime threshold = LocalDateTime.now().minusMonths(3);
        List<User> dormantTargets = userRepository.findAllByStatusInAndLastLoginAtBefore(
                List.of(UserStatus.ACTIVE, UserStatus.DORMANT_MANUAL),
                threshold
        );
        for (User user : dormantTargets) {
            user.changeStatus(UserStatus.DORMANT_AUTO);
        }

        log.info("✅ 자동 휴면 전환 완료 - 대상 수: {}, 기준일: {}", dormantTargets.size(), threshold.toLocalDate());
    }
}

