package moviebuddy;

import moviebuddy.data.CsvMovieReader;
import moviebuddy.domain.MovieReader;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Objects;

public class JdkDynamicProxyTests {

    @Test
    void useDynamicProxy() throws Exception {

        CsvMovieReader movieReader = new CsvMovieReader();
        movieReader.setResourceLoader(new DefaultResourceLoader());
        movieReader.setMetadata("movie_metadata.csv");
        movieReader.afterPropertiesSet(); // 올바른 메타데이터인지 검증

        // 프락시 생성
        // 인자1. ClassLoader
        // 인자2. 프록시를 생성할 인터페이스
        // 인자3. 프락시의 메소드가 호출되었을 때 그 실행 메소드를 제어하기 위한 Invocation 핸들러.
        ClassLoader classLoader = JdkDynamicProxyTests.class.getClassLoader();
        Class<?>[] interfaces = new Class[] {MovieReader.class};
        InvocationHandler handler = new PerformanceInvocationHandler(movieReader); // movieReader = target

        MovieReader proxy = (MovieReader) Proxy.newProxyInstance(classLoader, interfaces, handler);

        proxy.loadMovies(); // proxy = PerformanceInvocationHandler 호출됨.
        proxy.loadMovies();
    }

    static class PerformanceInvocationHandler implements InvocationHandler{

        final Logger log = LoggerFactory.getLogger(getClass());
        final Object target; // target = MovieReader

        PerformanceInvocationHandler(Object target){
            this.target = Objects.requireNonNull(target);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            long start = System.currentTimeMillis();

            // method : proxy객체로 호출한 loadMovies()
            // target : PerformanceInvocationHandler 생성 시 주입해준 MovieReader
            Object result = method.invoke(target, args);

            long elapsed = System.currentTimeMillis() - start;

            log.info("Excecution {} method finished in {} ms", method.getName(), elapsed);

            return result; // 결과값을 돌려준다.
        }
    }
}
