# dwpbank MoveWP3 Kafka Composite Key Serde

![Build status](https://travis-ci.com/movewp3/kafka-compoundkey-serde.svg?branch=master) ![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.dwpbank.movewp3/kafka-compoundkey-serde/badge.svg)


Using Avro-based keys for Kafka messages is tricky as the schema version id is part of the byte sequence that Kafka uses to determine key-based identity for that message (relevant for compaction), as well as partitioning when using the default partitioner. Unfortunately, the schema version id can change for schema changes that would not otherwise affect binary compatibility of the serialized keys.

This library is an attempt to ease using string-based keys derived from POJOs that expose composite key (i.e. key composed of multiple scalars).

## Usage

To use this library, first add the following dependency to your POM:

```                                
<dependency>
    <groupId>io.dwpbank.movewp3</groupId>
    <artifactId>kafka-compoundkey-serde</artifactId>
    <version>${movewp3-kafka-compoundkey-serde.version}</version>
</dependency>
```

Adding composite key support to a POJO is as easy as implementing the `CompositeKeyAware` interface, overriding the `compoundKeyAttributes` method and returning the relevant key attributes from it. For an example, refer to the [`CompoundKeyAwareTest`](src/test/java/io/dwpbank/movewp3/kafka/compoundkey/CompoundKeyAwareTest.java) implementation.

## Contributing

Pull requests are welcome. In order to make sure that your change can be easily merged, please follow these steps:

* Develop your changes in a feature branch named `feature/...`
* Base your feature branch on `master`
* Open your pull request against `master`
* Don't forget to implement tests

In case of any questions, feel open an issue in this project to discuss intended changes upfront.

