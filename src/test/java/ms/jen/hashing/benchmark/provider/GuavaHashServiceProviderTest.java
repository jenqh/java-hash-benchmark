package ms.jen.hashing.benchmark.provider;

import com.google.common.collect.ImmutableMap;
import ms.jen.hashing.benchmark.worker.GuavaHashWorker;
import ms.jen.hashing.benchmark.worker.HashWorker;

public class GuavaHashServiceProviderTest extends HashServiceProviderTest {

  public static final ImmutableMap<HashAlgorithm, Class<? extends HashWorker>>
      SUPPORTED_ALGORITHMS =
          ImmutableMap.<HashAlgorithm, Class<? extends HashWorker>>builder()
              .put(HashAlgorithm.ADLER32, GuavaHashWorker.class)
              .put(HashAlgorithm.CRC32, GuavaHashWorker.class)
              .put(HashAlgorithm.CRC32C, GuavaHashWorker.class)
              .put(HashAlgorithm.MD5, GuavaHashWorker.class)
              .put(HashAlgorithm.MURMUR3_32, GuavaHashWorker.class)
              .put(HashAlgorithm.MURMUR3_128, GuavaHashWorker.class)
              .put(HashAlgorithm.SHA1, GuavaHashWorker.class)
              .put(HashAlgorithm.SHA256, GuavaHashWorker.class)
              .put(HashAlgorithm.SHA384, GuavaHashWorker.class)
              .put(HashAlgorithm.SHA512, GuavaHashWorker.class)
              .put(HashAlgorithm.SIP_HASH_24, GuavaHashWorker.class)
              .build();

  @Override
  HashServiceProvider hashServiceProvider() {
    return GuavaHashServiceProvider.INSTANCE;
  }

  @Override
  ImmutableMap<HashAlgorithm, Class<? extends HashWorker>> supportedAlgorithmsToWorkerClass() {
    return SUPPORTED_ALGORITHMS;
  }
}
