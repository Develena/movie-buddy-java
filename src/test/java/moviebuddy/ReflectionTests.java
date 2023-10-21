package moviebuddy;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionTests {

    @Test
    void objectCreateAndMethodCall() throws Exception {

        // Without Reflection(보편적으로 객체의 메소드를 호출하는 방식)
        Duck duck = new Duck();
        duck.quack();

        // With Reflection
        // 1. 특정 패키지의 특정 클래스 이름의 클래스를 찾는다.
        Class<?> duckClass = Class.forName("moviebuddy.ReflectionTests$Duck");
        // 2. 그 클래스의 인스턴스를 생성한다(다음은 기본 생성자)
        Object duckObject = duckClass.getDeclaredConstructor().newInstance();
        // 3. 생성한 인스턴스에서 선언된 quack이라는 이름의 메소드를 찾는다. (인자는 없다)
        Method quackMethod = duckObject.getClass().getDeclaredMethod("quack", new Class<?>[0]);
        // 4. 선언된 메소드를 호출하는데, 이때 앞서 만든 인스턴스인 duckObject 인스턴스의 quack메소드를 호출한다.
        quackMethod.invoke(duckObject);

    }

    static class Duck{
        void quack() {
            System.out.println("꽥꽥");
        }
    }
}
