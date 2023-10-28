package moviebuddy;

import com.github.benmanes.caffeine.cache.Caffeine;
import moviebuddy.cache.CachingAdvice;
import moviebuddy.domain.MovieReader;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import javax.cache.annotation.CacheResult;
import java.util.concurrent.TimeUnit;

@Configuration // 이 클래스를 빈 구성 정보(bean metadata)로 선언하겠다.
@PropertySource("/application.properties") // classpath root부터 시작
@ComponentScan(basePackages = "moviebuddy")
@Import({ MovieBuddyFactory.DomainModuleConfig.class, MovieBuddyFactory.DataSourceModuleConfig.class}) // 다른 클래스에서 빈 구성 정보를 불러오기 위해 사용
public class MovieBuddyFactory {

    @Bean
    public Jaxb2Marshaller jaxb2Marshaller(){
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setPackagesToScan("moviebuddy");
        return marshaller;
    }

    // 캐싱 솔루션이 변경될 경우(Ex.RedisCacheManager) 이 부분이 변경된다.
    @Bean
    public CaffeineCacheManager caffeineCacheManager(){
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder().expireAfterWrite(3, TimeUnit.SECONDS));
        return cacheManager;
    }

    // Proxy를 자동으로 등록해주기 위한 빈 등록
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
        return new DefaultAdvisorAutoProxyCreator();
    }

    // Caching에 대한 부가기능을 제공하는 어드바이저 등록
    @Bean
    public Advisor cachingAdvisor(CacheManager cacheManager){
        // AnotationMatching 포인트컷
        AnnotationMatchingPointcut pointcut = new AnnotationMatchingPointcut(null, CacheResult.class);
        // --> Class 레벨에는 지정하지 않고, Method 레벨에 @CacheResult라는 어노테이션이 달린 pointcut 으로 지정하겠다.

        // 메소드 이름으로 타게팅하는 포인트컷
//        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
//        pointcut.setMappedName("load*");

        Advice advice = new CachingAdvice(cacheManager);

        // Advisor = Advice(부가기능) + Pointcut(대상 선정 알고리즘)
        return new DefaultPointcutAdvisor(pointcut, advice);
    }

    // 중첩 클래스
    @Configuration
    static class DomainModuleConfig {

    }

    @Configuration
    static class DataSourceModuleConfig {

        @Primary // 두 개 이상의 동일 타입 빈이 존재할 때, 이 빈을 우선하겠다.
        @Bean
        public ProxyFactoryBean cachingMovieReaderFactory(ApplicationContext applicationContext){

            MovieReader target = applicationContext.getBean(MovieReader.class);
            CacheManager cacheManager = applicationContext.getBean(CacheManager.class);

            ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
            proxyFactoryBean.setTarget(target);
            // 클래스 프록시 활성화(true), 비활성화(false, default) : 클래스타입 주입은 권장하지 않음.
//            proxyFactoryBean.setProxyTargetClass(true);
            proxyFactoryBean.addAdvice(new CachingAdvice(cacheManager));

            return proxyFactoryBean;

        }
    }
}
