package ms.jen.hashing.benchmark.worker;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;

/** A {@link HashWorker} wrapper for the Guava hashing library. */
public class GuavaHashWorker implements HashWorker {

  public static GuavaHashWorker of(HashFunction hashFunction) {
    return new GuavaHashWorker(hashFunction.newHasher());
  }

  private final Hasher hasher;

  private GuavaHashWorker(Hasher hasher) {
    this.hasher = hasher;
  }

  @Override
  public void update(byte... bytes) {
    hasher.putBytes(bytes);
  }

  @Override
  public HashCode getResult() {
    return hasher.hash();
  }
}
