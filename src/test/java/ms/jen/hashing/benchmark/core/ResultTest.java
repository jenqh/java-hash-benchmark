package ms.jen.hashing.benchmark.core;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.hash.HashCode;
import ms.jen.hashing.benchmark.provider.HashAlgorithm;
import ms.jen.hashing.benchmark.provider.ProviderName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ResultTest {

  private static final ProviderName PROVIDER_NAME = ProviderName.JAVA;
  private static final HashAlgorithm HASH_ALGORITHM = HashAlgorithm.ADLER32;
  private static final long DURATION_MILLIS = 24674L;
  private static final long DATA_SIZE = 1024L * 1024L;
  private static final int BUFFER_SIZE = 4096;
  private static final double MB_PER_SECOND = 12.5;
  private static final HashCode HASH_CODE = HashCode.fromInt(1);

  private Result result;

  @BeforeEach
  void setUp() {
    result =
        new Result(
            PROVIDER_NAME,
            HASH_ALGORITHM,
            DATA_SIZE,
            BUFFER_SIZE,
            DURATION_MILLIS,
            MB_PER_SECOND,
            HASH_CODE);
  }

  @Test
  void members() {
    assertThat(result.provider).isEqualTo(PROVIDER_NAME);
    assertThat(result.algorithm).isEqualTo(HASH_ALGORITHM);
    assertThat(result.durationMillis).isEqualTo(DURATION_MILLIS);
    assertThat(result.dataSize).isEqualTo(DATA_SIZE);
    assertThat(result.mbPerSecond).isEqualTo(MB_PER_SECOND);
    assertThat(result.hashCode).isEqualTo(HASH_CODE);
  }

  @Test
  void string() {
    assertThat(result.toString())
        .isEqualTo(
            "Result{"
                + "provider="
                + PROVIDER_NAME.name()
                + ", algorithm="
                + HASH_ALGORITHM.name()
                + ", dataSize="
                + DATA_SIZE
                + ", bufferSize="
                + BUFFER_SIZE
                + ", durationMillis="
                + DURATION_MILLIS
                + ", mbPerSecond="
                + String.format("%.3f", MB_PER_SECOND)
                + ", hashCode="
                + HASH_CODE.toString()
                + "}");
  }
}
