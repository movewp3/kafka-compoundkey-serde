package io.dwpbank.movewp3.kafka.compoundkey;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

public class CompoundKeyAwareTest {

  @Test
  public void suppliesCompoundKey() {
    var compundKeyAware = new SampleCompoundKeyAware("Hello", "world!", true);
    assertThat(compundKeyAware.toCompoundKey().toString()).isEqualTo("5:Hello-6:world!-4:true");
  }

  private static class SampleCompoundKeyAware implements CompoundKeyAware {

    private String attr1, attr2;
    private boolean flag1;

    public SampleCompoundKeyAware(String attr1, String attr2, boolean flag1) {
      this.attr1 = attr1;
      this.attr2 = attr2;
      this.flag1 = flag1;
    }

    @Override
    public List<Object> compoundKeyAttributes() {
      return Arrays.asList(attr1, attr2, flag1);
    }
  }
}
