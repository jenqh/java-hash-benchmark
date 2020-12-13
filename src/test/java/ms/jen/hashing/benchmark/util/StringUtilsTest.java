package ms.jen.hashing.benchmark.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import com.google.common.collect.ImmutableMap;
import java.util.stream.Collectors;
import ms.jen.hashing.benchmark.provider.HashAlgorithm;
import ms.jen.hashing.benchmark.provider.ProviderName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

class StringUtilsTest {

  private static final ImmutableMap<Long, String> BYTES_TO_STRING =
      ImmutableMap.<Long, String>builder()
          .put(0L, "0 B")
          .put(1L, "1 B")
          .put(1023L, "1023 B")
          .put(1024L, "1.00 KB")
          .put(1025L, "1.00 KB")
          .put(1023L * 1024L, "1023.00 KB")
          .put(1024L * 1024L, "1.00 MB")
          .put(1024L * 1024L + 8000L, "1.01 MB")
          .put(1024L * 1024L * 1024L, "1.00 GB")
          .put(1024L * 1024L * 1024L * 1024L, "1.00 TB")
          .put(1024L * 1024L * 1024L * 1024L * 1024L, "1.00 PB")
          .put(1024L * 1024L * 1024L * 1024L * 1024L * 1024L, "1.00 EB")
          .build();

  private static final ImmutableMap<Double, String> MB_PER_SECOND_TO_STRING =
      ImmutableMap.<Double, String>builder()
          .put((double) 1, "1.000")
          .put(1.5, "1.500")
          .put(1.53, "1.530")
          .put(1.536, "1.536")
          .put(1.5361, "1.536")
          .put(1.5367, "1.537")
          .build();

  @Test
  void algorithmNotProvidedErrorMessage() {
    String expected = "The HashAlgorithm MURMUR3_128 is not provided by HashServiceProvider JAVA.";
    String actual =
        StringUtils.algorithmNotProvidedErrorMessage(ProviderName.JAVA, HashAlgorithm.MURMUR3_128);
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void humanReadableBytesSI_nonNegative() {
    assertThatIllegalArgumentException().isThrownBy(() -> StringUtils.humanReadableBytesSI(-1));
  }

  @TestFactory
  Iterable<DynamicTest> humanReadableBytesSITests() {
    return BYTES_TO_STRING.keySet().stream()
        .map(
            bytes ->
                DynamicTest.dynamicTest(
                    "humanReadableBytesSI_" + BYTES_TO_STRING.get(bytes).replace(" ", "_"),
                    () ->
                        assertThat(StringUtils.humanReadableBytesSI(bytes))
                            .isEqualTo(BYTES_TO_STRING.get(bytes))))
        .collect(Collectors.toList());
  }

  @TestFactory
  Iterable<DynamicTest> mbPerSecondFormatTests() {
    return MB_PER_SECOND_TO_STRING.keySet().stream()
        .map(
            mbPerSecond ->
                DynamicTest.dynamicTest(
                    "mbPerSecondFormat_" + MB_PER_SECOND_TO_STRING.get(mbPerSecond),
                    () ->
                        assertThat(StringUtils.mbPerSecondFormat(mbPerSecond))
                            .isEqualTo(MB_PER_SECOND_TO_STRING.get(mbPerSecond))))
        .collect(Collectors.toList());
  }
}
