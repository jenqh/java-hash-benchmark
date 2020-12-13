package ms.jen.hashing.benchmark.provider;

/**
 * Represents hash algorithms available in this project.
 *
 * <p>Note that an algorithm may be provided by multiple providers.
 */
public enum HashAlgorithm {

  // Keep alphabetically sorted
  ADLER32(32),
  CRC32(32),
  CRC32C(32),
  MD2(128),
  MD5(128),
  MURMUR3_32(32),
  MURMUR3_128(128),
  SHA1(160),
  SHA256(256),
  SHA3_224(224),
  SHA3_256(256),
  SHA3_384(384),
  SHA3_512(512),
  SHA384(384),
  SHA512_224(224),
  SHA512_256(256),
  SHA512(512),
  SIP_HASH_24(64),
  XXHASH32(32),
  XXHASH64(64);

  /** Number of bits in the hash result. */
  private final int bits;

  HashAlgorithm(int bits) {
    this.bits = bits;
  }

  public int bits() {
    return bits;
  }
}
