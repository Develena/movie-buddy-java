package moviebuddy;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import moviebuddy.data.CsvMovieReader;
import moviebuddy.domain.Movie;
import org.checkerframework.checker.units.qual.C;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.*;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import java.util.List;
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

    // 중첩 클래스
    @Configuration
    static class DomainModuleConfig {

    }

    @Configuration
    static class DataSourceModuleConfig {

//        @Bean
//        public CsvMovieReader csvMovieReader(){
//            CacheManager cacheManager = new CaffeineCacheManager();
//            return new CsvMovieReader(cacheManager);
//        }
    }
}
