package moviebuddy;

/**
 * @author springrunner.kr@gmail.com
 *
 * Application 수행 시 오류가 있을 때, 오류를 예외 객체로 잡아서 외부에 전파.
 */
@SuppressWarnings("serial")
public class ApplicationException extends RuntimeException {

    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public static class CommandNotFoundException extends ApplicationException {

        public CommandNotFoundException() {
            super("command not found.");
        }

    }

    public static class UndefinedCommandActionException extends ApplicationException {

        public UndefinedCommandActionException() {
            super("command action is undefined.");
        }

    }

    public static class InvalidCommandArgumentsException extends ApplicationException {

        public InvalidCommandArgumentsException() {
            super("input error, please try again!");
        }

        public InvalidCommandArgumentsException(String message) {
            super(message);
        }

        public InvalidCommandArgumentsException(Throwable cause) {
            super(String.format("%s: %s", cause.getClass().getSimpleName(), cause.getMessage()), cause);
        }

    }

}