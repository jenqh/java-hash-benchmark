package ms.jen.hashing.benchmark.worker;

import com.google.common.hash.HashCode;

/**
 * A worker that executes the hashing task for streamed data.
 *
 * <p>Note that a new instance must be created for each object or stream of data to hash. This class
 * is NOT designed to be reused.
 */
public interface HashWorker {

  /** Update the worker when more streamed data is available. */
  void update(byte... bytes);

  /** Gets the hash result when all data has been fed to {@link #update(byte...)}. */
  HashCode getResult();
}
