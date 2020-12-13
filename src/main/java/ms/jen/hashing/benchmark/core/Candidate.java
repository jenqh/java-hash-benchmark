package ms.jen.hashing.benchmark.core;

import com.google.common.collect.ImmutableMap;
import ms.jen.hashing.benchmark.provider.ApacheHashServiceProvider;
import ms.jen.hashing.benchmark.provider.GuavaHashServiceProvider;
import ms.jen.hashing.benchmark.provider.HashAlgorithm;
import ms.jen.hashing.benchmark.provider.HashServiceProvider;
import ms.jen.hashing.benchmark.provider.JavaHashServiceProvider;
import ms.jen.hashing.benchmark.provider.JpountzHashServiceProvider;
import ms.jen.hashing.benchmark.provider.ProviderName;
import ms.jen.hashing.benchmark.util.StringUtils;
import ms.jen.hashing.benchmark.worker.HashWorker;

/** A class that represents a benchmark candidate. */
public class Candidate {

  private static final ImmutableMap<ProviderName, HashServiceProvider> PROVIDERS =
      ImmutableMap.<ProviderName, HashServiceProvider>builder()
          .put(ProviderName.GUAVA, GuavaHashServiceProvider.INSTANCE)
          .put(ProviderName.JAVA, JavaHashServiceProvider.INSTANCE)
          .put(ProviderName.APACHE, ApacheHashServiceProvider.INSTANCE)
          .put(ProviderName.JPOUNTZ, JpountzHashServiceProvider.INSTANCE)
          .build();

  private final ProviderName providerName;
  private final HashAlgorithm hashAlgorithm;

  public Candidate(ProviderName providerName, HashAlgorithm hashAlgorithm) {
    this.providerName = providerName;
    this.hashAlgorithm = hashAlgorithm;
    if (!PROVIDERS.containsKey(providerName)) {
      throw new IllegalArgumentException(
          "HashServiceProvider does not exist for provider: " + providerName.name());
    }
    if (!PROVIDERS.get(providerName).hasHashAlgorithm(hashAlgorithm)) {
      throw new IllegalArgumentException(
          StringUtils.algorithmNotProvidedErrorMessage(providerName, hashAlgorithm));
    }
  }

  public HashWorker createHashWorker() {
    return PROVIDERS.get(providerName).createHashWorker(hashAlgorithm);
  }

  public ProviderName getProviderName() {
    return providerName;
  }

  public HashAlgorithm getHashAlgorithm() {
    return hashAlgorithm;
  }
}
