package ms.jen.hashing.benchmark.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.hash.HashCode;
import java.time.Clock;
import java.util.List;
import java.util.function.Consumer;
import ms.jen.hashing.benchmark.provider.HashAlgorithm;
import ms.jen.hashing.benchmark.provider.ProviderName;
import ms.jen.hashing.benchmark.worker.HashWorker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BenchmarkTest {

  private static final long DATA_SIZE = 1024L * 1024L;
  private static final int BUFFER_SIZE = 1024;

  private Benchmark benchmark;

  @Mock private Clock mockClock;
  @Mock private HashWorker mockHashWorker;
  @Mock private Candidate mockCandidate1;
  @Mock private Candidate mockCandidate2;
  @Mock private Consumer<Result> mockResultConsumer;
  @Captor private ArgumentCaptor<Result> resultCaptor;

  @BeforeEach
  void setUp() {
    benchmark =
        Benchmark.newBuilder()
            .setDataSize(DATA_SIZE)
            .setBufferSize(BUFFER_SIZE)
            .addCandidate(ProviderName.JAVA, HashAlgorithm.ADLER32)
            .addCandidate(ProviderName.GUAVA, HashAlgorithm.SHA1)
            .addResultConsumer(mockResultConsumer)
            .build();
    ReflectionHelpers.setField(benchmark, "clock", mockClock);
  }

  @Test
  void getters() {
    assertThat(benchmark.getDataSize()).isEqualTo(DATA_SIZE);
    assertThat(benchmark.getBufferSize()).isEqualTo(BUFFER_SIZE);
    assertThat(benchmark.getResultConsumers()).containsExactly(mockResultConsumer);
  }

  @Test
  void candidatesCanBeCreated() {
    List<Candidate> candidates = benchmark.getCandidates();
    assertThat(candidates).hasSize(2);

    Candidate candidate1 = candidates.get(0);
    assertThat(candidate1.getProviderName()).isEqualTo(ProviderName.JAVA);
    assertThat(candidate1.getHashAlgorithm()).isEqualTo(HashAlgorithm.ADLER32);

    Candidate candidate2 = candidates.get(1);
    assertThat(candidate2.getProviderName()).isEqualTo(ProviderName.GUAVA);
    assertThat(candidate2.getHashAlgorithm()).isEqualTo(HashAlgorithm.SHA1);
  }

  @Test
  void call() {
    when(mockClock.millis()).thenReturn(0L).thenReturn(1000L).thenReturn(2000L).thenReturn(2100L);
    when(mockHashWorker.getResult())
        .thenReturn(HashCode.fromInt(1))
        .thenReturn(HashCode.fromInt(2));
    when(mockCandidate1.getProviderName()).thenReturn(ProviderName.JAVA);
    when(mockCandidate1.getHashAlgorithm()).thenReturn(HashAlgorithm.ADLER32);
    when(mockCandidate1.createHashWorker()).thenReturn(mockHashWorker);
    when(mockCandidate2.getProviderName()).thenReturn(ProviderName.GUAVA);
    when(mockCandidate2.getHashAlgorithm()).thenReturn(HashAlgorithm.SHA1);
    when(mockCandidate2.createHashWorker()).thenReturn(mockHashWorker);
    ReflectionHelpers.setField(
        benchmark, "candidates", ImmutableList.of(mockCandidate1, mockCandidate2));

    List<Result> results = benchmark.call();

    assertThat(results).hasSize(2);

    Result result1 = results.get(0);
    assertThat(result1.provider).isEqualTo(ProviderName.JAVA);
    assertThat(result1.algorithm).isEqualTo(HashAlgorithm.ADLER32);
    assertThat(result1.dataSize).isEqualTo(DATA_SIZE);
    assertThat(result1.durationMillis).isEqualTo(1000L);
    assertThat(result1.mbPerSecond).isEqualTo(1);
    assertThat(result1.hashCode).isEqualTo(HashCode.fromInt(1));

    Result result2 = results.get(1);
    assertThat(result2.provider).isEqualTo(ProviderName.GUAVA);
    assertThat(result2.algorithm).isEqualTo(HashAlgorithm.SHA1);
    assertThat(result2.dataSize).isEqualTo(DATA_SIZE);
    assertThat(result2.durationMillis).isEqualTo(100L);
    assertThat(result2.mbPerSecond).isEqualTo(10);
    assertThat(result2.hashCode).isEqualTo(HashCode.fromInt(2));

    verify(mockResultConsumer, times(2)).accept(resultCaptor.capture());
    assertThat(resultCaptor.getAllValues()).containsExactlyElementsOf(results);
  }
}
