package io.dwpbank.movewp3.kafka.compoundkey;

import java.util.Map;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serdes.WrapperSerde;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

/**
 * A Kafka serde for {@link CompoundKey}s.
 *
 * Compound keys are transmitted as UTF-8-encoded strings over the wire.
 */
public class CompoundKeySerde extends WrapperSerde<CompoundKey> {

  private static final String ENCODING_KEY = "serializer.encoding";
  private static final String ENCODING_UTF8 = "UTF-8";

  public CompoundKeySerde() {
    super(new CompoundKeySerializer(), new CompoundKeyDeserializer());
  }

  static class CompoundKeySerializer implements Serializer<CompoundKey> {

    private final StringSerializer delegate = new StringSerializer();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
      if (!isKey) {
        throw new UnsupportedOperationException(
            "CompoundKeySerializer only supports keys, but a value was provided for serialization instead");
      }

      delegate.configure(Map.of(ENCODING_KEY, ENCODING_UTF8), isKey);
    }

    @Override
    public byte[] serialize(String topic, CompoundKey data) {
      return delegate.serialize(topic, data.toString());
    }

    @Override
    public void close() {
      delegate.close();
    }
  }

  static class CompoundKeyDeserializer implements Deserializer<CompoundKey> {

    private final StringDeserializer delegate = new StringDeserializer();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
      if (!isKey) {
        throw new UnsupportedOperationException(
            "CompoundKeyDeserializer only supports keys, but a value was provided for de-serialization instead");
      }

      delegate.configure(Map.of(ENCODING_KEY, ENCODING_UTF8), isKey);
    }

    @Override
    public CompoundKey deserialize(String topic, byte[] data) {
      return new CompoundKey(delegate.deserialize(topic, data));
    }

    @Override
    public void close() {
      delegate.close();
    }
  }
}
