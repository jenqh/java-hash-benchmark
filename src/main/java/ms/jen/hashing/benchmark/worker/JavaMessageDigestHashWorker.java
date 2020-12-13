package ms.jen.hashing.benchmark.worker;

import com.google.common.hash.HashCode;
import java.security.MessageDigest;

/** A {@link HashWorker} wrapper for the Java {@link MessageDigest} class. */
public class JavaMessageDigestHashWorker implements HashWorker {

  private final MessageDigest messageDigest;

  public JavaMessageDigestHashWorker(MessageDigest messageDigest) {
    this.messageDigest = messageDigest;
  }

  @Override
  public void update(byte... bytes) {
    messageDigest.update(bytes);
  }

  @Override
  public HashCode getResult() {
    return HashCode.fromBytes(messageDigest.digest());
  }
}
