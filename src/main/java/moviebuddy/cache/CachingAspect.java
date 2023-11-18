package moviebuddy.cache;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Objects;

@Aspect
public class CachingAspect {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final CacheManager cacheManager;

    public CachingAspect(CacheManager cacheManager){
        this.cacheManager = Objects.requireNonNull(cacheManager);
    }


    /**
     * Advice 작성 : @Around
     * PointCut 작성 : Advice 내부 속성으로
     *
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around("target(moviebuddy.domain.MovieReader)")
    public Object doCachingReturnValue(ProceedingJoinPoint pjp) throws Throwable {

        // if there is cached value, return it right away.
        Cache cache = cacheManager.getCache(pjp.getThis().getClass().getName());
        Object cachedValue = cache.get(pjp.getSignature().getName(), Object.class);

        if(Objects.nonNull(cachedValue)){
            logger.info("returns cached data. [{}]", cachedValue);
            return cachedValue;
        }

        // if there isn't, delegate to target object and save returning value and return.
        cachedValue = pjp.proceed();
        cache.put(pjp.getSignature().getName(), cachedValue);
        logger.info("caching return value.[{}]", pjp);

        return cachedValue;

    }



}
