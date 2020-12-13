package ms.jen.hashing.benchmark.provider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import com.google.common.collect.ImmutableMap;
import java.util.Arrays;
import java.util.stream.Collectors;
import ms.jen.hashing.benchmark.worker.HashWorker;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

abstract class HashServiceProviderTest {

  abstract HashServiceProvider hashServiceProvider();

  abstract ImmutableMap<HashAlgorithm, Class<? extends HashWorker>>
      supportedAlgorithmsToWorkerClass();

  @TestFactory
  Iterable<DynamicTest> createHashWorker() {
    return Arrays.stream(HashAlgorithm.values())
        .map(this::newCreateHashWorkerTest)
        .collect(Collectors.toList());
  }

  private DynamicTest newCreateHashWorkerTest(HashAlgorithm algorithm) {
    if (supportedAlgorithmsToWorkerClass().containsKey(algorithm)) {
      return newCreateHashWorkerTest_success(algorithm);
    } else {
      return newCreateHashWorkerTest_error_notProvided(algorithm);
    }
  }

  private DynamicTest newCreateHashWorkerTest_success(HashAlgorithm algorithm) {
    return dynamicTest(
        String.format(
            "Algorithm %s is provided by %s",
            algorithm.name(), hashServiceProvider().getClass().getSimpleName()),
        () ->
            assertThat(hashServiceProvider().createHashWorker(algorithm))
                .isInstanceOf(supportedAlgorithmsToWorkerClass().get(algorithm)));
  }

  private DynamicTest newCreateHashWorkerTest_error_notProvided(HashAlgorithm algorithm) {
    return dynamicTest(
        String.format(
            "Algorithm %s is NOT provided by %s",
            algorithm.name(), hashServiceProvider().getClass().getSimpleName()),
        () ->
            assertThatIllegalArgumentException()
                .isThrownBy(() -> hashServiceProvider().createHashWorker(algorithm)));
  }
}
