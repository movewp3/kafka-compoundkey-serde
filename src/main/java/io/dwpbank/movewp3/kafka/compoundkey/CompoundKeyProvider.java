package io.dwpbank.movewp3.kafka.compoundkey;

import java.util.List;

/**
 * Extends an arbitrary POJO with the capability to derive a {@link CompoundKey} from it.
 *
 * <p>
 * Implementers need to supply an implementation of {@link #compoundKeyAttributes()} to be able to derive the attributes making up the key.
 * </p>
 */
@FunctionalInterface
public interface CompoundKeyProvider {

  /**
   * Returns the key attributes making up the compound key to be constructed.
   *
   * @return a list of objects
   */
  List<Object> compoundKeyAttributes();

  /**
   * Obtains a {@link CompoundKey} from this {@link CompoundKeyProvider}. Internally, the default implementation makes use of the key
   * attributes returned by {@link #compoundKeyAttributes()} to construct the resulting compound key.
   *
   * @return the corresponding {@link CompoundKey}
   */
  default CompoundKey toCompoundKey() {
    return CompoundKey.of(this::compoundKeyAttributes);
  }
}
