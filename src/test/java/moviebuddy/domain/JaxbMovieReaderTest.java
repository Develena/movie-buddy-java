package moviebuddy.domain;

import moviebuddy.MovieBuddyFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

@SpringJUnitConfig(MovieBuddyFactory.class)
public class JaxbMovieReaderTest {

    final JaxbMovieReader movieReader;

    @Autowired
    JaxbMovieReaderTest(JaxbMovieReader movieReader){
        this.movieReader = movieReader;
    }

    @Test
    void NotEmpty_LoadedMovies() {
        JaxbMovieReader movieReader = new JaxbMovieReader();
        List<Movie> movies = movieReader.loadMovies();

        Assertions.assertEquals(1375, movies.size());
    }

}
