package ms.jen.hashing.benchmark.provider;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.ImmutableMap;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class HashAlgorithmTest {

  private static final ImmutableMap<HashAlgorithm, Integer> BITS =
      ImmutableMap.<HashAlgorithm, Integer>builder()
          .put(HashAlgorithm.ADLER32, 32)
          .put(HashAlgorithm.CRC32, 32)
          .put(HashAlgorithm.CRC32C, 32)
          .put(HashAlgorithm.MD2, 128)
          .put(HashAlgorithm.MD5, 128)
          .put(HashAlgorithm.MURMUR3_32, 32)
          .put(HashAlgorithm.MURMUR3_128, 128)
          .put(HashAlgorithm.SHA1, 160)
          .put(HashAlgorithm.SHA256, 256)
          .put(HashAlgorithm.SHA384, 384)
          .put(HashAlgorithm.SHA3_224, 224)
          .put(HashAlgorithm.SHA3_256, 256)
          .put(HashAlgorithm.SHA3_384, 384)
          .put(HashAlgorithm.SHA3_512, 512)
          .put(HashAlgorithm.SHA512_224, 224)
          .put(HashAlgorithm.SHA512_256, 256)
          .put(HashAlgorithm.SHA512, 512)
          .put(HashAlgorithm.SIP_HASH_24, 64)
          .put(HashAlgorithm.XXHASH32, 32)
          .put(HashAlgorithm.XXHASH64, 64)
          .build();

  @Test
  void coversAllHashAlgorithms() {
    assertThat(BITS.keySet()).containsExactlyInAnyOrder(HashAlgorithm.values());
  }

  @ParameterizedTest(name = "{index} {0} has {1} bits")
  @MethodSource("algorithmsAndBits")
  void hasCorrectBits(HashAlgorithm hashAlgorithm, int bit) {
    assertThat(hashAlgorithm.bits()).isEqualTo(bit);
  }

  private static Stream<Arguments> algorithmsAndBits() {
    return BITS.keySet().stream().map(algorithm -> Arguments.of(algorithm, BITS.get(algorithm)));
  }
}
