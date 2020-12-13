package ms.jen.hashing.benchmark.worker;

import com.google.common.hash.HashCode;
import net.jpountz.xxhash.StreamingXXHash64;
import net.jpountz.xxhash.XXHashFactory;

/** A {@link HashWorker} wrapper for the jpountz's {@link StreamingXXHash64} class. */
public class JpountzStreaming64HashWorker implements HashWorker {

  private final StreamingXXHash64 function;

  public JpountzStreaming64HashWorker(XXHashFactory factory) {
    function = factory.newStreamingHash64(0L);
  }

  @Override
  public void update(byte... bytes) {
    function.update(bytes, 0, bytes.length);
  }

  @Override
  public HashCode getResult() {
    return HashCode.fromLong(function.getValue());
  }
}
