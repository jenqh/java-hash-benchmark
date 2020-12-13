package ms.jen.hashing.benchmark.provider;

import com.google.common.collect.ImmutableMap;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import ms.jen.hashing.benchmark.worker.GuavaHashWorker;
import ms.jen.hashing.benchmark.worker.HashWorker;

/**
 * A {@link HashServiceProvider} that creates {@link HashWorker} objects with Guava's hash
 * algorithms.
 */
public final class GuavaHashServiceProvider implements HashServiceProvider {

  public static final GuavaHashServiceProvider INSTANCE = new GuavaHashServiceProvider();

  private static final ImmutableMap<HashAlgorithm, HashFunction> GUAVA_HASH_FUNCTIONS =
      ImmutableMap.<HashAlgorithm, HashFunction>builder()
          .put(HashAlgorithm.ADLER32, Hashing.adler32())
          .put(HashAlgorithm.CRC32, Hashing.crc32())
          .put(HashAlgorithm.CRC32C, Hashing.crc32c())
          .put(HashAlgorithm.MD5, Hashing.md5())
          .put(HashAlgorithm.MURMUR3_32, Hashing.murmur3_32())
          .put(HashAlgorithm.MURMUR3_128, Hashing.murmur3_128())
          .put(HashAlgorithm.SHA1, Hashing.sha1())
          .put(HashAlgorithm.SHA256, Hashing.sha256())
          .put(HashAlgorithm.SHA384, Hashing.sha384())
          .put(HashAlgorithm.SHA512, Hashing.sha512())
          .put(HashAlgorithm.SIP_HASH_24, Hashing.sipHash24())
          .build();

  private GuavaHashServiceProvider() {}

  @Override
  public boolean hasHashAlgorithm(HashAlgorithm algorithm) {
    return GUAVA_HASH_FUNCTIONS.containsKey(algorithm);
  }

  @Override
  public HashWorker createHashWorker(HashAlgorithm algorithm) {
    checkHasHashAlgorithm(algorithm);
    return GuavaHashWorker.of(GUAVA_HASH_FUNCTIONS.get(algorithm));
  }
}
