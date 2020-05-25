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
    if (stringBuilder.length() > 0) {
      stringBuilder.append('-');
    }

    if (value == null) {
      return appendNull();
    } else if (value instanceof BigDecimal) {
      return appendString(((BigDecimal) value).toPlainString());
    }

    return appendString(value.toString());
  }

  private CompoundKeyStringBuilder appendNull() {
    stringBuilder.append("N");
    return this;
  }

  private CompoundKeyStringBuilder appendString(String str) {
    stringBuilder.append(str.length()).append(':').append(str);
    return this;
  }

  @Override
  public String toString() {
    return this.stringBuilder.toString();
  }
}
