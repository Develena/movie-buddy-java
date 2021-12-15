package moviebuddy.domain;

import java.util.List;

/**
 * @author springrunner.kr@gmail.com
 */
public class MovieFinderTest {

	public static void main(String[] args) {
//		MovieBuddyApplication application = new MovieBuddyApplication();

		MovieFinder movieFinder = new MovieFinder(new CsvMovieReader());

		// 마이클 베이 감독의 작품은 3편인 것을 테스트함.
		List<Movie> result = movieFinder.directedBy("Michael Bay");
		assertEquals(3, result.size());

		// 2015년도에 출시된 영화는 225편인 것을 테스트함.
		result = movieFinder.releasedYearBy(2015);
    	assertEquals(225, result.size());
	}
	
	static void assertEquals(long expected, long actual) {
		if (expected != actual) {
			throw new RuntimeException(String.format("actual(%d) is different from the expected(%d)", actual, expected));			
		}
	}
	
}
