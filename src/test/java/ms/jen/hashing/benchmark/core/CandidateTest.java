package ms.jen.hashing.benchmark.core;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.List;
import ms.jen.hashing.benchmark.provider.ApacheHashServiceProviderTest;
import ms.jen.hashing.benchmark.provider.GuavaHashServiceProviderTest;
import ms.jen.hashing.benchmark.provider.HashAlgorithm;
import ms.jen.hashing.benchmark.provider.JavaHashServiceProviderTest;
import ms.jen.hashing.benchmark.provider.JpountzHashServiceProviderTest;
import ms.jen.hashing.benchmark.provider.ProviderName;
import ms.jen.hashing.benchmark.worker.HashWorker;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

class CandidateTest {

  private static final ImmutableMap<
          ProviderName, ImmutableMap<HashAlgorithm, Class<? extends HashWorker>>>
      PROVIDER_TO_SUPPORTED_ALGORITHMS =
          ImmutableMap
              .<ProviderName, ImmutableMap<HashAlgorithm, Class<? extends HashWorker>>>builder()
              .put(ProviderName.JAVA, JavaHashServiceProviderTest.SUPPORTED_ALGORITHMS)
              .put(ProviderName.GUAVA, GuavaHashServiceProviderTest.SUPPORTED_ALGORITHMS)
              .put(
                  ProviderName.APACHE,
                  ApacheHashServiceProviderTest.SUPPORTED_ALGORITHMS)
              .put(ProviderName.JPOUNTZ, JpountzHashServiceProviderTest.SUPPORTED_ALGORITHMS)
              .build();

  @TestFactory
  Iterable<DynamicTest> createTestWorker_all() {
    List<DynamicTest> tests = new ArrayList<>();
    for (ProviderName providerName : ProviderName.values()) {
      ImmutableMap<HashAlgorithm, Class<? extends HashWorker>> supportedAlgorithms =
          PROVIDER_TO_SUPPORTED_ALGORITHMS.get(providerName);
      for (HashAlgorithm hashAlgorithm : supportedAlgorithms.keySet()) {
        Class<? extends HashWorker> hashWorkerClass = supportedAlgorithms.get(hashAlgorithm);
        tests.add(newCreateTestWorkerTest(providerName, hashAlgorithm, hashWorkerClass));
      }
    }
    return tests;
  }

  private static DynamicTest newCreateTestWorkerTest(
      ProviderName providerName,
      HashAlgorithm hashAlgorithm,
      Class<? extends HashWorker> hashWorkerClass) {
    return DynamicTest.dynamicTest(
        String.format(
            "Candidate can be created with Provider %s and Algorithm %s",
            providerName.name(), hashAlgorithm.name()),
        () -> {
          Candidate candidate = new Candidate(providerName, hashAlgorithm);
          assertThat(candidate.createHashWorker()).isInstanceOf(hashWorkerClass);
          assertThat(candidate.getProviderName()).isEqualTo(providerName);
          assertThat(candidate.getHashAlgorithm()).isEqualTo(hashAlgorithm);
        });
  }
}
