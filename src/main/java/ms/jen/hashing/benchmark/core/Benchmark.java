package ms.jen.hashing.benchmark.core;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.hash.HashCode;
import java.time.Clock;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import ms.jen.hashing.benchmark.provider.HashAlgorithm;
import ms.jen.hashing.benchmark.provider.ProviderName;
import ms.jen.hashing.benchmark.worker.HashWorker;

/** The class that performs the benchmarking on a list of providers and algorithms. */
public class Benchmark implements Callable<List<Result>> {

  /** The builder for {@link Benchmark}. */
  public static class Builder {

    private long dataSize;
    private int bufferSize;
    private final List<Candidate> candidates;
    private final List<Consumer<Result>> resultConsumers;

    public Builder() {
      candidates = new ArrayList<>();
      resultConsumers = new ArrayList<>();
    }

    public Builder setDataSize(long dataSize) {
      this.dataSize = dataSize;
      return this;
    }

    public Builder setBufferSize(int bufferSize) {
      this.bufferSize = bufferSize;
      return this;
    }

    public Builder addCandidate(ProviderName providerName, HashAlgorithm hashAlgorithm) {
      candidates.add(new Candidate(providerName, hashAlgorithm));
      return this;
    }

    public Builder addResultConsumer(Consumer<Result>... rcs) {
      resultConsumers.addAll(Arrays.asList(rcs));
      return this;
    }

    public Builder addResultConsumer(List<Consumer<Result>> rcs) {
      resultConsumers.addAll(rcs);
      return this;
    }

    public Benchmark build() {
      return new Benchmark(dataSize, bufferSize, candidates, resultConsumers);
    }
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  private final long dataSize;
  private final int bufferSize;
  private final ImmutableList<Candidate> candidates;
  private final ImmutableList<Consumer<Result>> resultConsumers;
  private final int rounds;
  private final Clock clock;

  private Benchmark(
      long dataSize,
      int bufferSize,
      List<Candidate> candidates,
      List<Consumer<Result>> resultConsumers) {
    Preconditions.checkArgument(dataSize > 0, "dataSize must be a positive number");
    Preconditions.checkArgument(bufferSize > 0, "buffer size must be a positive number");
    Preconditions.checkArgument(
        dataSize >= bufferSize, "dataSize must be no smaller than bufferSize");
    Preconditions.checkArgument(
        candidates != null && candidates.size() > 0, "candidates must be present");
    Preconditions.checkNotNull(resultConsumers);
    this.dataSize = dataSize;
    this.bufferSize = bufferSize;
    this.candidates = ImmutableList.copyOf(candidates);
    this.resultConsumers = ImmutableList.copyOf(resultConsumers);
    this.rounds = (int) Math.ceil((double) dataSize / (double) bufferSize);
    this.clock = Clock.systemUTC();
  }

  public List<Result> call() {
    List<Result> results = new ArrayList<>();
    for (Candidate candidate : candidates) {
      byte[] buffer = new byte[bufferSize];
      long before = clock.millis();
      HashWorker hashWorker = candidate.createHashWorker();
      for (int i = 0; i < rounds; i++) {
        hashWorker.update(buffer);
      }
      long after = clock.millis();
      long duration = after - before;
      double mbPerSecond = ((double) dataSize / (1024 * 1024)) / ((double) duration / 1000);
      HashCode hashCode = hashWorker.getResult();
      Result result =
          new Result(
              candidate.getProviderName(),
              candidate.getHashAlgorithm(),
              dataSize,
              bufferSize,
              duration,
              mbPerSecond,
              hashCode);
      for (Consumer<Result> resultConsumer : resultConsumers) {
        resultConsumer.accept(result);
      }
      results.add(result);
    }
    return results;
  }

  public long getDataSize() {
    return dataSize;
  }

  public int getBufferSize() {
    return bufferSize;
  }

  public ImmutableList<Candidate> getCandidates() {
    return candidates;
  }

  public ImmutableList<Consumer<Result>> getResultConsumers() {
    return resultConsumers;
  }
}
