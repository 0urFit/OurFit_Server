package project1.OurFit.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LoginAttemptService {

    private static final int MAX_ATTEMPTS = 5;
    private static final int BLOCK_DURATION_MINUTES = 1;
    private static final long BLOCK_DURATION = BLOCK_DURATION_MINUTES * 60 * 1000; // 분을 밀리초로 변환

    private Map<String, Integer> ipAttempts = new ConcurrentHashMap<>();
    private Map<String, Long> ipBlockList = new ConcurrentHashMap<>();

    private Map<String, Integer> emailAttempts = new ConcurrentHashMap<>();
    private Map<String, Long> emailBlockList = new ConcurrentHashMap<>();

    public void incrementAttempts(String ip, String email) {
        incrementIpAttempts(ip);
        incrementEmailAttempts(email);
    }

    private void incrementIpAttempts(String ip) {
        int ipAttemptsCount = ipAttempts.getOrDefault(ip, 0) + 1;
        ipAttempts.put(ip, ipAttemptsCount);

        if (ipAttemptsCount >= MAX_ATTEMPTS)
            ipBlockList.put(ip, System.currentTimeMillis() + BLOCK_DURATION);
    }

    private void incrementEmailAttempts(String email) {
        int emailAttemptsCount = emailAttempts.getOrDefault(email, 0) + 1;
        emailAttempts.put(email, emailAttemptsCount);

        if (emailAttemptsCount >= MAX_ATTEMPTS)
            emailBlockList.put(email, System.currentTimeMillis() + BLOCK_DURATION);
    }

    public boolean isBlocked(String ip, String email) {
        return isIpBlocked(ip) || isEmailBlocked(email);
    }

    private boolean isIpBlocked(String ip) {
        Long ipBlockEndTime = ipBlockList.get(ip);
        boolean isIpBlocked = ipBlockEndTime != null && System.currentTimeMillis() < ipBlockEndTime;

        if (isIpBlocked) {
            ipBlockList.put(ip, System.currentTimeMillis() + BLOCK_DURATION);
            ipAttempts.put(ip, MAX_ATTEMPTS);
        }

        return isIpBlocked;
    }

    private boolean isEmailBlocked(String email) {
        Long emailBlockEndTime = emailBlockList.get(email);
        boolean isEmailBlocked = emailBlockEndTime != null && System.currentTimeMillis() < emailBlockEndTime;

        if (isEmailBlocked) {
            emailBlockList.put(email, System.currentTimeMillis() + BLOCK_DURATION);
            emailAttempts.put(email, MAX_ATTEMPTS);
        }

        return isEmailBlocked;
    }

    public void resetAttempts(String ip, String email) {
        ipAttempts.remove(ip);
        ipBlockList.remove(ip);
        emailAttempts.remove(email);
        emailBlockList.remove(email);
    }
}
