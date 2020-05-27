package io.dwpbank.movewp3.kafka.compoundkey;

import io.dwpbank.movewp3.kafka.compoundkey.CompoundKeySerde.CompoundKeyDeserializer;
import java.util.Objects;

/**
 * A compound key capable of representing the key fields of a POJO (implementing {@link CompoundKeyProvider}) as a unique string.
 */
public class CompoundKey {

  private final String key;

  /**
   * Constructs a compound key from a pre-existing sting. Used by the {@link CompoundKeyDeserializer}.
   *
   * @param key the string-serialized key to turn back into a compound key
   */
  CompoundKey(String key) {
    this.key = key;
  }

  /**
   * Constructs a compound key from a {@link CompoundKeyStringBuilder}. Used when deriving a compound key from a {@link CompoundKeyProvider}.
   *
   * @param builder a builder that has already been equipped with the state / key information to be represented by this compound key
   */
  CompoundKey(CompoundKeyStringBuilder builder) {
    this(builder.toString());
  }

  /**
   * Constrcuts a {@link CompoundKey} from a {@link CompoundKeyProvider}.
   *
   * @param compoundKeyProvider the {@link CompoundKeyProvider} to derive the {@link CompoundKey} from
   * @return the derived compound key
   */
  public static CompoundKey of(CompoundKeyProvider compoundKeyProvider) {
    CompoundKeyStringBuilder builder = new CompoundKeyStringBuilder();

    for (Object partialKey : compoundKeyProvider.compoundKeyAttributes()) {
      builder.append(partialKey);
    }

    return new CompoundKey(builder);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CompoundKey that = (CompoundKey) o;
    return key.equals(that.key);
  }

  @Override
  public int hashCode() {
    return Objects.hash(key);
  }

  @Override
  public String toString() {
    return key;
  }
}
