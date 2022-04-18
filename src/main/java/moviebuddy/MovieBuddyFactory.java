package moviebuddy;

import moviebuddy.domain.CsvMovieReader;
import moviebuddy.domain.MovieFinder;
import moviebuddy.domain.MovieReader;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;

@Configuration // 이 클래스를 빈 구성 정보(bean metadata)로 선언하겠다.
@Import({ MovieBuddyFactory.DomainModuleConfig.class, MovieBuddyFactory.DataSourceModuleConfig.class}) // 다른 클래스에서 빈 구성 정보를 불러오기 위해 사용
public class MovieBuddyFactory {

    // 중첩 클래스
    @Configuration
    static class DomainModuleConfig {
        @Bean // 어떤 빈이 있는가를 나타내기 위함.
        public MovieFinder movieFinder(MovieReader movieReader){
            return new MovieFinder(movieReader);
        }
    }

    @Configuration
    static class DataSourceModuleConfig {
        @Bean
        public MovieReader movieReader(){
            return new CsvMovieReader();
        }
    }
}
