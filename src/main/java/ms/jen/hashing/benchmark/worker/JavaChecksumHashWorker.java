package ms.jen.hashing.benchmark.worker;

import com.google.common.hash.HashCode;
import java.util.zip.Checksum;

/** A {@link HashWorker} wrapper for the Java {@link Checksum} class. */
public class JavaChecksumHashWorker implements HashWorker {

  private final Checksum checksum;

  public JavaChecksumHashWorker(Checksum checksum) {
    this.checksum = checksum;
  }

  @Override
  public void update(byte... bytes) {
    checksum.update(bytes, 0, bytes.length);
  }

  @Override
  public HashCode getResult() {
    return HashCode.fromInt((int) checksum.getValue());
  }
}
