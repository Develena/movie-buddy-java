package moviebuddy.data;

import moviebuddy.domain.Movie;
import moviebuddy.domain.MovieReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;

import java.util.ArrayList;
import java.util.List;

public class CachingMovieReaderTest {

    @Test
    void caching(){
        CacheManager cacheManager = new ConcurrentMapCacheManager(); // 테스트용 캐시매니저
        MovieReader target = new DummyMovieReader(); // 테스트를 위한 더미 클래스.

        CachingMovieReader movieReader = new CachingMovieReader(cacheManager, target);

        Assertions.assertNull(movieReader.getCachedData()); // 현재 캐시된 데이터가 null임을 확인.

        List<Movie> movies = movieReader.loadMovies(); // 대상 객체로부터 호출한 값을 캐싱.
        Assertions.assertNotNull(movieReader.getCachedData()); // 캐시된 데이터가 있기 때문에 Null이 아님.(emptyList라도)
        Assertions.assertSame(movieReader.loadMovies(), movies); // loadMovies()로 가져온 데이터 = 캐시된 데이터 일치 확인.

    }

    class DummyMovieReader implements MovieReader{

        @Override
        public List<Movie> loadMovies() {
            return new ArrayList<>();
        }
    }
}
