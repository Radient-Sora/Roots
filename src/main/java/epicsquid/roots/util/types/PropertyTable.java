package epicsquid.roots.util.types;

import java.util.HashMap;

public class PropertyTable extends HashMap<Property<?>, Object> {
  public PropertyTable() {
  }

  public void addProperties (Property<?>... properties) {
    for (Property prop : properties) {
      this.put(prop, null);
    }
  }

  public <T> T getProperty (Property<T> property) {
    T result = property.cast(get(property));
    if (result == null) return property.defaultValue;
    return result;
  }

  public <T> void setProperty (Property<T> property, T value) {
    this.put(property, value);
  }

  public boolean hasProperty (Property<?> property) {
    return this.containsKey(property);
  }
}
