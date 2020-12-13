package ms.jen.hashing.benchmark.provider;

import com.google.common.collect.ImmutableMap;
import java.security.MessageDigest;
import java.util.function.Supplier;
import ms.jen.hashing.benchmark.worker.HashWorker;
import ms.jen.hashing.benchmark.worker.JavaMessageDigestHashWorker;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * A {@link HashServiceProvider} that creates {@link HashWorker} objects with Apache Commons Codec's
 * hash algorithms.
 */
public class ApacheHashServiceProvider implements HashServiceProvider {

  public static ApacheHashServiceProvider INSTANCE =
      new ApacheHashServiceProvider();

  private static final ImmutableMap<HashAlgorithm, Supplier<MessageDigest>> SUPPORTED_ALGORITHMS =
      ImmutableMap.<HashAlgorithm, Supplier<MessageDigest>>builder()
          .put(HashAlgorithm.MD2, DigestUtils::getMd2Digest)
          .put(HashAlgorithm.MD5, DigestUtils::getMd5Digest)
          .put(HashAlgorithm.SHA1, DigestUtils::getSha1Digest)
          .put(HashAlgorithm.SHA256, DigestUtils::getSha256Digest)
          .put(HashAlgorithm.SHA3_224, DigestUtils::getSha3_224Digest)
          .put(HashAlgorithm.SHA3_256, DigestUtils::getSha3_256Digest)
          .put(HashAlgorithm.SHA3_384, DigestUtils::getSha3_384Digest)
          .put(HashAlgorithm.SHA3_512, DigestUtils::getSha3_512Digest)
          .put(HashAlgorithm.SHA384, DigestUtils::getSha384Digest)
          .put(HashAlgorithm.SHA512_224, DigestUtils::getSha512_224Digest)
          .put(HashAlgorithm.SHA512_256, DigestUtils::getSha512_256Digest)
          .put(HashAlgorithm.SHA512, DigestUtils::getSha3_512Digest)
          .build();

  private ApacheHashServiceProvider() {}

  @Override
  public boolean hasHashAlgorithm(HashAlgorithm algorithm) {
    return SUPPORTED_ALGORITHMS.containsKey(algorithm);
  }

  @Override
  public HashWorker createHashWorker(HashAlgorithm algorithm) {
    checkHasHashAlgorithm(algorithm);
    return new JavaMessageDigestHashWorker(SUPPORTED_ALGORITHMS.get(algorithm).get());
  }
}
