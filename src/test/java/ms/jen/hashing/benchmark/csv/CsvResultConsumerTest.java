package ms.jen.hashing.benchmark.csv;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.hash.HashCode;
import ms.jen.hashing.benchmark.core.Result;
import ms.jen.hashing.benchmark.provider.HashAlgorithm;
import ms.jen.hashing.benchmark.provider.ProviderName;
import ms.jen.hashing.benchmark.util.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CsvResultConsumerTest {

  private static final ProviderName PROVIDER_NAME = ProviderName.JAVA;
  private static final HashAlgorithm HASH_ALGORITHM = HashAlgorithm.ADLER32;
  private static final long DURATION_MILLIS = 24674L;
  private static final long DATA_SIZE = 1024L * 1024L;
  private static final int BUFFER_SIZE = 4096;
  private static final double MB_PER_SECOND = 12;
  private static final HashCode HASH_CODE = HashCode.fromInt(1);

  private Result result;
  private StringBuilder stringBuilder;
  private CsvResultConsumer csvResultConsumer;

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
    stringBuilder = new StringBuilder();
  }

  @Test
  void accept_withHeader() {
    csvResultConsumer = new CsvResultConsumer(stringBuilder, /* withHeader= */ true);
    csvResultConsumer.accept(result);
    String expected =
        "Provider,"
            + "Algorithm,"
            + "Data Size,"
            + "Buffer Size,"
            + "Duration (ms),"
            + "Speed (MB/s),"
            + "Hash Size (bits),"
            + "Hash Result"
            + "\r\n"
            + PROVIDER_NAME.name()
            + ","
            + HASH_ALGORITHM.name()
            + ","
            + StringUtils.humanReadableBytesSI(DATA_SIZE)
            + ","
            + BUFFER_SIZE
            + ","
            + DURATION_MILLIS
            + ","
            + StringUtils.mbPerSecondFormat(MB_PER_SECOND)
            + ","
            + HASH_ALGORITHM.bits()
            + ","
            + HASH_CODE
            + "\r\n";

    String actual = stringBuilder.toString();

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void accept_withoutHeader() {
    csvResultConsumer = new CsvResultConsumer(stringBuilder, /* withHeader= */ false);
    csvResultConsumer.accept(result);
    String expected =
        PROVIDER_NAME.name()
            + ","
            + HASH_ALGORITHM.name()
            + ","
            + StringUtils.humanReadableBytesSI(DATA_SIZE)
            + ","
            + BUFFER_SIZE
            + ","
            + DURATION_MILLIS
            + ","
            + StringUtils.mbPerSecondFormat(MB_PER_SECOND)
            + ","
            + HASH_ALGORITHM.bits()
            + ","
            + HASH_CODE
            + "\r\n";

    String actual = stringBuilder.toString();

    assertThat(actual).isEqualTo(expected);
  }
}
