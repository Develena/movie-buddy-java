package moviebuddy;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

public class CaffeineTest {

    @Test
    void useCache() throws InterruptedException {
        // Cache 객체 생성
        Cache<String,Object> cache = Caffeine.newBuilder()
                .expireAfterWrite(200, TimeUnit.MILLISECONDS) // 일정시간 이후 만료시키기
                .maximumSize(100) // 100개만 저장
                .build();

        // key,value 정의
        String key = "springrunner";
        Object value = new Object();

        Assertions.assertNull(cache.getIfPresent(key));

        cache.put(key, value);

        Assertions.assertEquals(value, cache.getIfPresent(key));

        TimeUnit.MILLISECONDS.sleep(100); // 100ms를 주고
        Assertions.assertEquals(value, cache.getIfPresent(key)); // 다시 꺼내서 비교해보기

        TimeUnit.MILLISECONDS.sleep(100); // 100ms를 더 주기
        // 실패 - 200ms 지났으므로 같지 않은 값.
//        Assertions.assertEquals(value, cache.getIfPresent(key));
        // 성공 - 200ms 지났으므로 비워진 캐시값.
        Assertions.assertNull(cache.getIfPresent(key));

    }
}
