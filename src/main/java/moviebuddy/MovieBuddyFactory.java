package moviebuddy;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.interceptor.*;
import org.springframework.context.annotation.*;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import java.util.concurrent.TimeUnit;

@Configuration // 이 클래스를 빈 구성 정보(bean metadata)로 선언하겠다.
@PropertySource("/application.properties") // classpath root부터 시작
@ComponentScan(basePackages = "moviebuddy")
@Import({ MovieBuddyFactory.DomainModuleConfig.class, MovieBuddyFactory.DataSourceModuleConfig.class}) // 다른 클래스에서 빈 구성 정보를 불러오기 위해 사용
//@EnableAspectJAutoProxy
@EnableCaching // 선언적 Caching 기능 사용
public class MovieBuddyFactory implements CachingConfigurer {

    @Bean
    public Jaxb2Marshaller jaxb2Marshaller(){
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setPackagesToScan("moviebuddy");
        return marshaller;
    }

    // 캐싱 솔루션이 변경될 경우(Ex.RedisCacheManager) 이 부분이 변경된다.
    @Bean
    public CaffeineCacheManager caffeineCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder().expireAfterWrite(3, TimeUnit.SECONDS));
        return cacheManager;
    }

//     Proxy를 자동으로 등록해주기 위한 빈 등록
//    @Bean
//    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
//        return new DefaultAdvisorAutoProxyCreator();
//    }

//     Caching에 대한 부가기능을 제공하는 어드바이저 등록
//    @Bean
//    public Advisor cachingAdvisor(CacheManager cacheManager){
//        // AnotationMatching 포인트컷
//        AnnotationMatchingPointcut pointcut = new AnnotationMatchingPointcut(null, CacheResult.class);
//        // --> Class 레벨에는 지정하지 않고, Method 레벨에 @CacheResult라는 어노테이션이 달린 pointcut 으로 지정하겠다.
//
//        // 메소드 이름으로 타게팅하는 포인트컷
////        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
////        pointcut.setMappedName("load*");
//
//        Advice advice = new CachingAdvice(cacheManager);
//
//        // Advisor = Advice(부가기능) + Pointcut(대상 선정 알고리즘)
//        return new DefaultPointcutAdvisor(pointcut, advice);
//    }
//
//    /**
//     * @EnableAspectJAutoProxy 를 추가함으로써 기존에 작성된 Advisor는 삭제한다.
//     * 그리고, 새로 작성한 CachingAspect을 빈으로 추가한다.
//     * ++ 선언적 캐싱(@EnableCaching) 기능 사용 후, CachingAspect는 필요 없어짐.
//     */
//    @Bean
//    public CachingAspect cachingAspect(CacheManager cacheManager){
//        return new CachingAspect(cacheManager);
//    }

    // 중첩 클래스
    @Configuration
    static class DomainModuleConfig {

    }

    @Configuration
    static class DataSourceModuleConfig {

//        @Primary // 두 개 이상의 동일 타입 빈이 존재할 때, 이 빈을 우선하겠다.
//        @Bean
//        public ProxyFactoryBean cachingMovieReaderFactory(ApplicationContext applicationContext){
//
//            MovieReader target = applicationContext.getBean(MovieReader.class);
//            CacheManager cacheManager = applicationContext.getBean(CacheManager.class);
//
//            ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
//            proxyFactoryBean.setTarget(target);
//            // 클래스 프록시 활성화(true), 비활성화(false, default) : 클래스타입 주입은 권장하지 않음.
////            proxyFactoryBean.setProxyTargetClass(true);
//            proxyFactoryBean.addAdvice(new CachingAdvice(cacheManager));
//
//            return proxyFactoryBean;
//
//        }
    }



    @Override
    public CacheManager cacheManager() {
        return caffeineCacheManager();
    }

    @Override
    public CacheResolver cacheResolver() {
        return new SimpleCacheResolver(caffeineCacheManager());
    }

    @Override
    public KeyGenerator keyGenerator() {
        return new SimpleKeyGenerator();
    }

    @Override
    public CacheErrorHandler errorHandler() {
        return new SimpleCacheErrorHandler();
    }
}
