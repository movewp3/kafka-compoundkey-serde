package io.dwpbank.movewp3.kafka.compoundkey;

import java.math.BigDecimal;

public class CompoundKeyStringBuilder {

  private final StringBuilder stringBuilder;

  CompoundKeyStringBuilder() {
    this(32);
  }

  CompoundKeyStringBuilder(int initialCapacity) {
    this.stringBuilder = new StringBuilder(initialCapacity);
  }

  public CompoundKeyStringBuilder append(Object value) {
    if (value == null) {
      return appendNullOrEmpty();
    } else if (value instanceof BigDecimal) {
      return appendString(((BigDecimal) value).toPlainString());
    }

    return appendString(value.toString());
  }

  private CompoundKeyStringBuilder appendNullOrEmpty() {
    stringBuilder.append("0:-");

    return this;
  }

  private CompoundKeyStringBuilder appendString(String str) {
    if (str.isEmpty()) {
      return appendNullOrEmpty();
    }

    stringBuilder.append(str.length()).append(':').append(str).append('-');

    return this;
  }

  @Override
  public String toString() {
    return this.stringBuilder.toString();
  }
}
