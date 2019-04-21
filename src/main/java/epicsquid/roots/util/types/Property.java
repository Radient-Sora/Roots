package epicsquid.roots.util.types;

public class Property<T> {
  public final Class<?> type;
  public final String name;
  public final T defaultValue;

  public Property (String name, T defaultValue) {
    this.type = defaultValue.getClass();
    this.name = name;
    this.defaultValue = defaultValue;
  }

  public T cast (Object val) {
    return (T) val;
  }
}
