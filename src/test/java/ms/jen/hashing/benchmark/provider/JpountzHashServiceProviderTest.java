package ms.jen.hashing.benchmark.provider;

import com.google.common.collect.ImmutableMap;
import ms.jen.hashing.benchmark.worker.HashWorker;
import ms.jen.hashing.benchmark.worker.JpountzStreaming32HashWorker;
import ms.jen.hashing.benchmark.worker.JpountzStreaming64HashWorker;

public class JpountzHashServiceProviderTest extends HashServiceProviderTest {

  public static final ImmutableMap<HashAlgorithm, Class<? extends HashWorker>>
      SUPPORTED_ALGORITHMS =
          ImmutableMap.<HashAlgorithm, Class<? extends HashWorker>>builder()
              .put(HashAlgorithm.XXHASH32, JpountzStreaming32HashWorker.class)
              .put(HashAlgorithm.XXHASH64, JpountzStreaming64HashWorker.class)
              .build();

  @Override
  HashServiceProvider hashServiceProvider() {
    return JpountzHashServiceProvider.INSTANCE;
  }

  @Override
  ImmutableMap<HashAlgorithm, Class<? extends HashWorker>> supportedAlgorithmsToWorkerClass() {
    return SUPPORTED_ALGORITHMS;
  }
}
