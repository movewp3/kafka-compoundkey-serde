package io.dwpbank.movewp3.kafka.compoundkey;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class CompoundKeySerdeTest {

  private static final String TOPIC = "topic";

  @Test
  public void testSerde() {
    try (var serde = new CompoundKeySerde()) {
      var compoundKey = CompoundKey.of(() -> List.of(42, "Hello", "world"));

      try (var serializer = serde.serializer(); var deserializer = serde.deserializer()) {
        serializer.configure(Map.of(), true);
        deserializer.configure(Map.of(), true);

        var bytes = serde.serializer().serialize(TOPIC, compoundKey);
        var deserializedCompoundKey = serde.deserializer().deserialize(TOPIC, bytes);

        assertThat(compoundKey).isEqualTo(deserializedCompoundKey);
      }
    }
  }

  @Test
  public void serializerRejectsNonKeyRecords() {
    try (var serde = new CompoundKeySerde()) {
      var serializer = serde.serializer();

      assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(() -> serializer.configure(Map.of(), false));
    }
  }

  @Test
  public void deserializerRejectsNonKeyRecords() {
    try (var serde = new CompoundKeySerde()) {
      var deserializer = serde.deserializer();

      assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(() -> deserializer.configure(Map.of(), false));
    }
  }

  @Test
  public void serializerEncodesStringAsUtf8() {
    try (var serde = new CompoundKeySerde()) {
      var compoundKey = CompoundKey.of(() -> List.of("Hellö", "wörld"));
      var bytes = serde.serializer().serialize(TOPIC, compoundKey);

      assertThat(bytes).isEqualTo(new byte[]{53, 58, 72, 101, 108, 108, -61, -74, 45, 53, 58, 119, -61, -74, 114, 108, 100});
    }
  }
}
