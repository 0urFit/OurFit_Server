package project1.OurFit.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LoginAttemptService {

    private static final int MAX_ATTEMPTS = 5;
    private static final int BLOCK_DURATION_MINUTES = 1;
    private static final long BLOCK_DURATION = BLOCK_DURATION_MINUTES * 60 * 1000; // 분을 밀리초로 변환

    private Map<String, Integer> attempts = new ConcurrentHashMap<>();
    private Map<String, Long> blockList = new ConcurrentHashMap<>();

    public void incrementAttempts(String ip) {
        int attemptsCount = attempts.getOrDefault(ip, 0) + 1;
        attempts.put(ip, attemptsCount);

        if (attemptsCount >= MAX_ATTEMPTS) {
            blockList.put(ip, System.currentTimeMillis() + BLOCK_DURATION);
        }
    }

    public boolean isBlocked(String ip) {
        Long blockEndTime = blockList.get(ip);
        if (blockEndTime == null) {
            return false;
        }
        if (System.currentTimeMillis() < blockEndTime) {
            return true;
        }
        // 차단 시간이 지났으면 차단 목록에서 제거
        blockList.remove(ip);
        attempts.remove(ip);
        return false;
    }

    public void resetAttempts(String ip) {
        attempts.remove(ip);
        blockList.remove(ip);
    }
}
