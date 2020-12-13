package ms.jen.hashing.benchmark.worker;

import com.google.common.hash.HashCode;
import net.jpountz.xxhash.StreamingXXHash32;
import net.jpountz.xxhash.XXHashFactory;

/** A {@link HashWorker} wrapper for the jpountz's {@link StreamingXXHash32} class. */
public class JpountzStreaming32HashWorker implements HashWorker {

  private final StreamingXXHash32 function;

  public JpountzStreaming32HashWorker(XXHashFactory factory) {
    function = factory.newStreamingHash32(0);
  }

  @Override
  public void update(byte... bytes) {
    function.update(bytes, 0, bytes.length);
  }

  @Override
  public HashCode getResult() {
    return HashCode.fromInt(function.getValue());
  }
}
