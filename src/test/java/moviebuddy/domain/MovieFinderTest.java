package moviebuddy.domain;

import moviebuddy.MovieBuddyFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @author springrunner.kr@gmail.com
 */
public class MovieFinderTest {

	final MovieBuddyFactory movieBuddyFactory = new MovieBuddyFactory();

	final MovieFinder movieFinder = movieBuddyFactory.movieFinder();

	@Test
	public void NotEmpty_directedBy(){
		// 마이클 베이 감독의 작품은 3편인 것을 테스트함.
		List<Movie> movies = movieFinder.directedBy("Michael Bay");
		Assertions.assertEquals(3, movies.size());
	}

	@Test
	public void NotEmpty_releasedYearBy(){
		// 2015년도에 출시된 영화는 225편인 것을 테스트함.
		List<Movie> movies = movieFinder.releasedYearBy(2015);
		Assertions.assertEquals(225, movies.size());
	}

}
