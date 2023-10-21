package moviebuddy.data;

import moviebuddy.MovieBuddyFactory;
import moviebuddy.MovieBuddyProfile;
import moviebuddy.domain.Movie;
import moviebuddy.domain.MovieReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.util.AopTestUtils;

import java.util.List;

@ActiveProfiles(MovieBuddyProfile.XML_MODE)
@SpringJUnitConfig(MovieBuddyFactory.class)
@TestPropertySource(properties = "movie.metadata=movie_metadata.xml")
public class XmlMovieReaderTest {

    final MovieReader movieReader;

    @Autowired
    XmlMovieReaderTest(MovieReader movieReader){
        this.movieReader = movieReader;
    }

    @Test
    void NotEmpty_LoadedMovies() {
        List<Movie> movies = movieReader.loadMovies();

        Assertions.assertEquals(1375, movies.size());
    }

    // 주입받은 MovieReader 타입 검증하기
    @Test
    void Check_MovieReaderType(){
        // 주입 받은 movieReader가 Proxy Object가 맞는가?
        Assertions.assertTrue(AopUtils.isAopProxy(movieReader));

        // 맞다면, Proxy Object를 꺼내와서
        MovieReader target = AopTestUtils.getTargetObject(movieReader);
        // Proxy Object 내부에 XmlMovieReader가 들어가있는지 확인함
        Assertions.assertTrue(XmlMovieReader.class.isAssignableFrom(target.getClass()));
    }

}
