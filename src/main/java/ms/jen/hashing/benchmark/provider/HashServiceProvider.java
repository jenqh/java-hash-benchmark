package ms.jen.hashing.benchmark.provider;

import ms.jen.hashing.benchmark.util.StringUtils;
import ms.jen.hashing.benchmark.worker.HashWorker;

/** A factory interface to create {@link HashWorker}s. */
public interface HashServiceProvider {

  /** Returns true if the provider supports this algorithm. */
  boolean hasHashAlgorithm(HashAlgorithm algorithm);

  /**
   * Creates a {@link HashWorker} for the given {@link HashAlgorithm}.
   *
   * @throws IllegalArgumentException if the provider does not support this algorithm.
   */
  HashWorker createHashWorker(HashAlgorithm algorithm);

  default void checkHasHashAlgorithm(HashAlgorithm algorithm) {
    if (!hasHashAlgorithm(algorithm)) {
      throw new IllegalArgumentException(
          StringUtils.algorithmNotProvidedErrorMessage(ProviderName.GUAVA, algorithm));
    }
  }
}
