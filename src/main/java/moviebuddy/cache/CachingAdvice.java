package moviebuddy.cache;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Objects;

// MethodInterceptor : method가 호출되었을 때 interceptor 해주는 기능.
public class CachingAdvice implements MethodInterceptor {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final CacheManager cacheManager;

    public CachingAdvice(CacheManager cacheManager){
        this.cacheManager = Objects.requireNonNull(cacheManager);
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        // 캐시된 데이터가 존재하면, 즉시 반환 처리
        Cache cache = cacheManager.getCache(invocation.getThis().getClass().getName());
        // getThis : 대상 객체를 의미(CsvMovieReader or XmlMovieReader)

        Object cachedValue = cache.get(invocation.getMethod().getName(), Object.class);
        if(Objects.nonNull(cachedValue)) {
            logger.info("===> return cached data.[{}]", invocation);
            return cachedValue;
        }

        // 캐시된 데이터가 없으면, 대상 객체에 명령을 위임하고 반환된 값을 캐시에 저장 후 반환 처리.
        cachedValue = invocation.proceed();
        cache.put(invocation.getMethod().getName(), cachedValue);
        logger.info("===> caching return value.[{}]", invocation);

        return cachedValue;
    }
}
