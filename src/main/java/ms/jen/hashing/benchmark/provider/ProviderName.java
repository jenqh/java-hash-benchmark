package ms.jen.hashing.benchmark.provider;

/** Represents hash algorithm providers used in this project. */
public enum ProviderName {

  /** For algorithms in package {@link com.google.common.hash} */
  GUAVA,

  /** For algorithms of {@link java.util.zip.Checksum} or {@link java.security.MessageDigest}. */
  JAVA,

  /** For algorithms in package @{@link org.apache.commons.codec.digest}. */
  APACHE,

  /** For algorithms in package {@link net.jpountz.xxhash} */
  JPOUNTZ,
}
