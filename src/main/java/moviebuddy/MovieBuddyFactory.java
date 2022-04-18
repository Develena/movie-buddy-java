package moviebuddy;

import moviebuddy.domain.CsvMovieReader;
import moviebuddy.domain.MovieFinder;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // 이 클래스를 빈 구성 정보(bean metadata)로 선언하겠다.
public class MovieBuddyFactory {

    @Bean // 어떤 빈이 있는가를 나타내기 위함.
    public MovieFinder movieFinder(){
        return new MovieFinder(new CsvMovieReader());
    }
}
