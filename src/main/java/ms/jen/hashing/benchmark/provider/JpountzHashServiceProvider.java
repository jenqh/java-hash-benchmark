package ms.jen.hashing.benchmark.provider;

import ms.jen.hashing.benchmark.util.StringUtils;
import ms.jen.hashing.benchmark.worker.HashWorker;
import ms.jen.hashing.benchmark.worker.JpountzStreaming32HashWorker;
import ms.jen.hashing.benchmark.worker.JpountzStreaming64HashWorker;
import net.jpountz.xxhash.XXHashFactory;

/**
 * A {@link HashServiceProvider} that creates {@link HashWorker} objects with jpountz's XXHash
 * algorithms.
 */
public final class JpountzHashServiceProvider implements HashServiceProvider {

  public static final JpountzHashServiceProvider INSTANCE = new JpountzHashServiceProvider();

  private final XXHashFactory factory;

  private JpountzHashServiceProvider() {
    factory = XXHashFactory.fastestInstance();
  }

  @Override
  public boolean hasHashAlgorithm(HashAlgorithm algorithm) {
    return algorithm == HashAlgorithm.XXHASH32 || algorithm == HashAlgorithm.XXHASH64;
  }

  @Override
  public HashWorker createHashWorker(HashAlgorithm algorithm) {
    switch (algorithm) {
      case XXHASH64:
        return new JpountzStreaming64HashWorker(factory);
      case XXHASH32:
        return new JpountzStreaming32HashWorker(factory);
      default:
        throw new IllegalArgumentException(
            StringUtils.algorithmNotProvidedErrorMessage(ProviderName.JPOUNTZ, algorithm));
    }
  }
}
