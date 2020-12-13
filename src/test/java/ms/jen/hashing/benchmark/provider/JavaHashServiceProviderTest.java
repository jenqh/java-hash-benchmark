package ms.jen.hashing.benchmark.provider;

import com.google.common.collect.ImmutableMap;
import ms.jen.hashing.benchmark.worker.HashWorker;
import ms.jen.hashing.benchmark.worker.JavaChecksumHashWorker;
import ms.jen.hashing.benchmark.worker.JavaMessageDigestHashWorker;

public class JavaHashServiceProviderTest extends HashServiceProviderTest {

  public static final ImmutableMap<HashAlgorithm, Class<? extends HashWorker>>
      SUPPORTED_ALGORITHMS =
          ImmutableMap.<HashAlgorithm, Class<? extends HashWorker>>builder()
              .put(HashAlgorithm.ADLER32, JavaChecksumHashWorker.class)
              .put(HashAlgorithm.CRC32, JavaChecksumHashWorker.class)
              .put(HashAlgorithm.CRC32C, JavaChecksumHashWorker.class)
              .put(HashAlgorithm.MD5, JavaMessageDigestHashWorker.class)
              .put(HashAlgorithm.SHA1, JavaMessageDigestHashWorker.class)
              .put(HashAlgorithm.SHA256, JavaMessageDigestHashWorker.class)
              .build();

  @Override
  HashServiceProvider hashServiceProvider() {
    return JavaHashServiceProvider.INSTANCE;
  }

  @Override
  ImmutableMap<HashAlgorithm, Class<? extends HashWorker>> supportedAlgorithmsToWorkerClass() {
    return SUPPORTED_ALGORITHMS;
  }
}
