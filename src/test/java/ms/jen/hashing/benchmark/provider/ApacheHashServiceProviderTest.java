package ms.jen.hashing.benchmark.provider;

import com.google.common.collect.ImmutableMap;
import ms.jen.hashing.benchmark.worker.HashWorker;
import ms.jen.hashing.benchmark.worker.JavaMessageDigestHashWorker;

public class ApacheHashServiceProviderTest extends HashServiceProviderTest {

  public static final ImmutableMap<HashAlgorithm, Class<? extends HashWorker>>
      SUPPORTED_ALGORITHMS =
          ImmutableMap.<HashAlgorithm, Class<? extends HashWorker>>builder()
              .put(HashAlgorithm.MD2, JavaMessageDigestHashWorker.class)
              .put(HashAlgorithm.MD5, JavaMessageDigestHashWorker.class)
              .put(HashAlgorithm.SHA1, JavaMessageDigestHashWorker.class)
              .put(HashAlgorithm.SHA256, JavaMessageDigestHashWorker.class)
              .put(HashAlgorithm.SHA3_224, JavaMessageDigestHashWorker.class)
              .put(HashAlgorithm.SHA3_256, JavaMessageDigestHashWorker.class)
              .put(HashAlgorithm.SHA3_384, JavaMessageDigestHashWorker.class)
              .put(HashAlgorithm.SHA3_512, JavaMessageDigestHashWorker.class)
              .put(HashAlgorithm.SHA384, JavaMessageDigestHashWorker.class)
              .put(HashAlgorithm.SHA512_224, JavaMessageDigestHashWorker.class)
              .put(HashAlgorithm.SHA512_256, JavaMessageDigestHashWorker.class)
              .put(HashAlgorithm.SHA512, JavaMessageDigestHashWorker.class)
              .build();

  @Override
  HashServiceProvider hashServiceProvider() {
    return ApacheHashServiceProvider.INSTANCE;
  }

  @Override
  ImmutableMap<HashAlgorithm, Class<? extends HashWorker>> supportedAlgorithmsToWorkerClass() {
    return SUPPORTED_ALGORITHMS;
  }
}
