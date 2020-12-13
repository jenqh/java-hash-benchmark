package ms.jen.hashing.benchmark.util;

import com.google.common.base.Preconditions;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import ms.jen.hashing.benchmark.provider.HashAlgorithm;
import ms.jen.hashing.benchmark.provider.ProviderName;

/** Utility methods related to {@link String}s. */
public final class StringUtils {

  public static String algorithmNotProvidedErrorMessage(
      ProviderName providerName, HashAlgorithm hashAlgorithm) {
    return String.format(
        "The HashAlgorithm %s is not provided by HashServiceProvider %s.",
        hashAlgorithm.name(), providerName.name());
  }

  public static String humanReadableBytesSI(long bytes) {
    Preconditions.checkArgument(bytes >= 0);
    if (bytes < 1024) {
      return bytes + " B";
    }
    long value = bytes;
    CharacterIterator ci = new StringCharacterIterator("KMGTPE");
    for (int i = 40; i >= 0 && bytes > 0xfffccccccccccccL >> i; i -= 10) {
      value >>= 10;
      ci.next();
    }
    value *= Long.signum(bytes);
    return String.format("%.2f %cB", value / 1024.0, ci.current());
  }

  public static String mbPerSecondFormat(double mbPerSecond) {
    return String.format("%.3f", mbPerSecond);
  }

  private StringUtils() {}
}
