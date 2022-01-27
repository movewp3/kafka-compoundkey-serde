package io.dwpbank.movewp3.kafka.compoundkey;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CompoundKeyStringBuilderTest {

  @Test
  public void compoundKeyStringBuilderShouldBeAccessible() {
    assertThat(CompoundKeyStringBuilder.of()).isNotNull();
  }

}