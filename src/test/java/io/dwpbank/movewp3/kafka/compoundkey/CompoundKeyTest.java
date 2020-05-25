package io.dwpbank.movewp3.kafka.compoundkey;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

public class CompoundKeyTest {

  @Test
  public void nullIsRenderedCorrectly() {
    assertThat(CompoundKey.of(() -> Arrays.asList(new Object[]{null})).toString()).isEqualTo("0:");
  }

  @Test
  public void multipleStrings() {
    assertThat(
        CompoundKey.of(() -> List.of("Gallia est omnis divisa in partes tres.", "Quarum unum incolunt Belgae, ..."))
            .toString()).isEqualTo("39:Gallia est omnis divisa in partes tres.-32:Quarum unum incolunt Belgae, ...");
  }

  @Test
  public void emptyString() {
    assertThat(CompoundKey.of(() -> List.of("")).toString()).isEqualTo("0:");
  }

  @Test
  public void boxedSimpleTypes() {
    assertThat(
        CompoundKey.of(() -> Arrays.asList(23, 42L, -1, true, false, 3.14))
            .toString()).isEqualTo("2:23-2:42-2:-1-4:true-5:false-4:3.14");
  }

  @Test
  public void bigDecimal() {
    assertThat(CompoundKey.of(() -> List.of(BigDecimal.ONE, BigDecimal.ZERO)).toString()).isEqualTo("1:1-1:0");
  }
}
