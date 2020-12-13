package ms.jen.hashing.benchmark.provider;

import com.google.common.collect.ImmutableMap;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.function.Supplier;
import java.util.zip.Adler32;
import java.util.zip.CRC32;
import java.util.zip.CRC32C;
import java.util.zip.Checksum;
import ms.jen.hashing.benchmark.util.StringUtils;
import ms.jen.hashing.benchmark.worker.HashWorker;
import ms.jen.hashing.benchmark.worker.JavaChecksumHashWorker;
import ms.jen.hashing.benchmark.worker.JavaMessageDigestHashWorker;

/**
 * A {@link HashServiceProvider} that creates {@link HashWorker} objects with Java's built-in hash
 * algorithms based on either {@link Checksum} or {@link MessageDigest}.
 */
public final class JavaHashServiceProvider implements HashServiceProvider {

  public static final JavaHashServiceProvider INSTANCE = new JavaHashServiceProvider();

  private static final ImmutableMap<HashAlgorithm, Supplier<Checksum>> JAVA_CHECKSUMS =
      ImmutableMap.<HashAlgorithm, Supplier<Checksum>>builder()
          .put(HashAlgorithm.ADLER32, Adler32::new)
          .put(HashAlgorithm.CRC32, CRC32::new)
          .put(HashAlgorithm.CRC32C, CRC32C::new)
          .build();

  private static final ImmutableMap<HashAlgorithm, String> JAVA_MESSAGE_DIGEST_NAMES;

  static {
    ImmutableMap<HashAlgorithm, String> all =
        ImmutableMap.<HashAlgorithm, String>builder()
            .put(HashAlgorithm.MD5, "MD5")
            .put(HashAlgorithm.SHA1, "SHA-1")
            .put(HashAlgorithm.SHA256, "SHA-256")
            .build();
    ImmutableMap.Builder<HashAlgorithm, String> available = ImmutableMap.builder();
    for (HashAlgorithm hashAlgorithm : all.keySet()) {
      try {
        String algorithmString = all.get(hashAlgorithm);
        MessageDigest.getInstance(algorithmString);
        available.put(hashAlgorithm, algorithmString);
      } catch (NoSuchAlgorithmException e) {
        // Do nothing
      }
    }
    JAVA_MESSAGE_DIGEST_NAMES = available.build();
  }

  private JavaHashServiceProvider() {}

  @Override
  public boolean hasHashAlgorithm(HashAlgorithm algorithm) {
    return JAVA_CHECKSUMS.containsKey(algorithm)
        || JAVA_MESSAGE_DIGEST_NAMES.containsKey(algorithm);
  }

  @Override
  public HashWorker createHashWorker(HashAlgorithm algorithm) {
    if (JAVA_CHECKSUMS.containsKey(algorithm)) {
      return new JavaChecksumHashWorker(JAVA_CHECKSUMS.get(algorithm).get());
    }
    if (JAVA_MESSAGE_DIGEST_NAMES.containsKey(algorithm)) {
      return new JavaMessageDigestHashWorker(
          getMessageDigestUnchecked(JAVA_MESSAGE_DIGEST_NAMES.get(algorithm)));
    }
    throw new IllegalArgumentException(
        StringUtils.algorithmNotProvidedErrorMessage(ProviderName.JAVA, algorithm));
  }

  private static MessageDigest getMessageDigestUnchecked(String algorithm) {
    try {
      return MessageDigest.getInstance(algorithm);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("This will never happen.");
    }
  }
}
