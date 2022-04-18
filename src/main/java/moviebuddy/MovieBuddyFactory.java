package moviebuddy;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration // 이 클래스를 빈 구성 정보(bean metadata)로 선언하겠다.
@ComponentScan
@Import({ MovieBuddyFactory.DomainModuleConfig.class, MovieBuddyFactory.DataSourceModuleConfig.class}) // 다른 클래스에서 빈 구성 정보를 불러오기 위해 사용
public class MovieBuddyFactory {

    // 중첩 클래스
    @Configuration
    static class DomainModuleConfig {

    }

    @Configuration
    static class DataSourceModuleConfig {

    }
}
