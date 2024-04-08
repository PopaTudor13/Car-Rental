package Domain;

public interface EntityConverter<T extends Entity> {
    String toString(T entity);
    T fromString(String string);
}
