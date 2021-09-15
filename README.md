# dwpbank MoveWP3 Kafka Composite Key Serde

[![Java CI](https://github.com/movewp3/kafka-compoundkey-serde/actions/workflows/build.yml/badge.svg)](https://github.com/movewp3/kafka-compoundkey-serde/actions/workflows/build.yml) [![Maven Central](https://img.shields.io/maven-central/v/io.dwpbank.movewp3/kafka-compoundkey-serde)](https://search.maven.org/artifact/io.dwpbank.movewp3/kafka-compoundkey-serde)


Using Avro-based keys for Kafka messages is tricky: the schema version id is part of the byte sequence that Kafka uses to determine key-based identity. Both the the default partitioner <sup id="s1">[[1]](#f1)</sup> as well as the log compactor only see this byte sequence including tha schema version id <sup id="s2">[[2]](#f2)</sup>. Unfortunately, the schema version id can change for schema changes that would not otherwise affect binary compatibility of the serialized keys, including backwards compatible schema changes. In consequence, records having the same logical avro key but different schema version ids will be distributed across different partitions. 

This library is an attempt to ease using string-based keys derived from POJOs that expose a composite key (i.e. key composed of multiple scalars). To promote decoupling serialized keys are to be viewed as opaque strings. Consequently, the deserialization implementation does not provide key decomposition.

<b id="f1">[[1]](#s2)</b> [`DefaultPartitioner.java`](https://github.com/confluentinc/kafka/blob/master/clients/src/main/java/org/apache/kafka/clients/producer/internals/DefaultPartitioner.java)

<b id="f2">[[2]](#s1)</b> [`AbstractKafkaAvroSerializer.java`](https://github.com/confluentinc/schema-registry/blob/master/avro-serializer/src/main/java/io/confluent/kafka/serializers/AbstractKafkaAvroSerializer.java)

## Usage

To add this library to your project include the following dependency in your POM:

```                                
<dependency>
    <groupId>io.dwpbank.movewp3</groupId>
    <artifactId>kafka-compoundkey-serde</artifactId>
    <version>${movewp3-kafka-compoundkey-serde.version}</version>
</dependency>
```

Then follow these three easy steps:

### (1) Implement the interface `CompoundKeyProvider` in your record type

```java
private interface SampleCompoundKey extends CompoundKeyProvider { 
  long tenantId();
  long customerId();

  @Override
  default List<Object> compoundKeyAttributes() {
    return List.of(tenantId(), customerId());
  }
}

private class SampleProducerRecord implements SampleCompoundKey {
  private long tenantId, customerId;
  private String customerName;

  public SampleProducerRecord(long tenantId, long customerId, String customerName) {
    this.tenantdId = tenantId; this.customerId = customerId; this.customerName = customerName;
  }

  public long tenantId() { return tenantId; }
  public long customerId() { return customerId; }
}
```

### (2) Use `CompoundKeySerde` to publish and receive records 

```java
public class CompoundKeyDemo {
  public static void main(Object[] args) {
    Properties props = new Properties();
    props.put("bootstrap.servers", "localhost:9092");
    props.put("key.serializer", io.dwpbank.movewp3.kafka.compoundkey.CompoundKeySerde.CompoundKeySerializer.class);
    props.put("value.serializer", io.confluent.kafka.serializers.KafkaAvroSerializer.class);
        
    try (Producer<SampleCompoundKey, SampleProducerRecord> producer = new KafkaProducer<>(props)) {
       SampleProducerRecord record = new SampleProducerRecord(4711, 815, "the customer"); 
       producer.send(new ProducerRecord<SampleProducerKey, SampleProducerRecord>("my-topic", record.toCompoundKey(), record));
    }
  }
```

### (3) Enjoy key encoding

The key will be serialized as the opaque length encoded string `"4:4711-3:815"`.

Another implementation example can be found in [`CompoundKeyProviderTest`](src/test/java/io/dwpbank/movewp3/kafka/compoundkey/CompoundKeyProviderTest.java) implementation.

## Encoding

Encoding is in UTF-8. The `CompoundKey`'s components are serialized using their `toString()` method except for `BigDecimal` using `BigDecimal.toPlainString()` and `null` values (explained in the grammar below).

### Grammar

```
SIZE ::= [0-9]{1,9}
COLON ::= ':'
DATA ::= (.*)
NULL ::= 'N'
PAYLOAD_ELEMENT ::= (NULL | SIZE COLON DATA)
payload ::= PAYLOAD_ELEMENT | ('-' PAYLOAD_ELEMENT)
```

## Alternatives

Alternatives are [netstrings](https://cr.yp.to/proto/netstrings.txt) and its derivative [tnetstrings](https://tnetstrings.info/). For our usecase, however, those are too mighty as they allow deserialization which we wanted to explicitly disallow. In addition we support `BigDecimal`.

## Contributing

Pull requests are welcome. In order to make sure that your change can be easily merged, please follow these steps:

* Develop your changes in a feature branch named `feature/...`
* Base your feature branch on `main`
* Open your pull request against `main`
* Don't forget to implement tests

In case of any questions, feel open an issue in this project to discuss intended changes upfront.
