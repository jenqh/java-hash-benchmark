package ms.jen.hashing.benchmark.core;

import com.google.common.base.MoreObjects;
import com.google.common.hash.HashCode;
import ms.jen.hashing.benchmark.provider.HashAlgorithm;
import ms.jen.hashing.benchmark.provider.ProviderName;

/** The result of a single benchmarking attempt. */
public class Result {

  public final ProviderName provider;
  public final HashAlgorithm algorithm;
  public final long dataSize;
  public final int bufferSize;
  public final long durationMillis;
  public final double mbPerSecond;
  public final HashCode hashCode;

  public Result(
      ProviderName provider,
      HashAlgorithm algorithm,
      long dataSize,
      int bufferSize,
      long durationMillis,
      double mbPerSecond,
      HashCode hashCode) {
    this.provider = provider;
    this.algorithm = algorithm;
    this.dataSize = dataSize;
    this.bufferSize = bufferSize;
    this.durationMillis = durationMillis;
    this.mbPerSecond = mbPerSecond;
    this.hashCode = hashCode;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("provider", provider)
        .add("algorithm", algorithm)
        .add("dataSize", dataSize)
        .add("bufferSize", bufferSize)
        .add("durationMillis", durationMillis)
        .add("mbPerSecond", String.format("%.3f", mbPerSecond))
        .add("hashCode", hashCode)
        .toString();
  }
}
