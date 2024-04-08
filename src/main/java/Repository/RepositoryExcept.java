package Repository;

public class RepositoryExcept extends Exception {
    public RepositoryExcept(String message) {
        super(message);
    }

    public RepositoryExcept(String message, Throwable cause) {
        super(message, cause);
    }
}
