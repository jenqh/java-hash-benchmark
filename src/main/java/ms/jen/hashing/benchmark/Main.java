package ms.jen.hashing.benchmark;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Callable;
import ms.jen.hashing.benchmark.core.Benchmark;
import ms.jen.hashing.benchmark.csv.CsvResultConsumer;
import ms.jen.hashing.benchmark.provider.HashAlgorithm;
import ms.jen.hashing.benchmark.provider.ProviderName;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

/** The main CLI entry point. */
@Command(
    name = "java-hash-benchmark",
    description = "run benchmarks to compare the performance of different algorithms",
    version = "1.1.0")
public class Main implements Callable<Integer> {

  private static final long DEFAULT_DATA_SIZE = 1024 * 1024 * 1024; // 1 GB;
  private static final int DEFAULT_BUFFER_SIZE = 4096;

  @Option(
      names = {"-d", "--data-size"},
      description =
          "[Optional] Total number of bytes to be fed into each of the hash algorithms; the "
              + "default value is 1073741824 (1 GB) if this option is not provided.")
  private long dataSize;

  @Option(
      names = {"-b", "--buffer-size"},
      description =
          "[Optional] Number of bytes to be fed into each hash algorithm in a single iteration; "
              + "the default value is 4096 if this option is not provided.")
  private int bufferSize;

  @Option(
      names = {"-o", "--output"},
      description =
          "[Optional] Path to the output CSV file. If this option is not provided, no output file "
              + "will be generated. If a file already exists at this path, the result (without "
              + "headers) will be appended to the end of the existing file. ")
  @SuppressWarnings("unused") // Assigned by the CLI framework
  private Path outputCsvPath;

  @Option(
      names = {"-h", "--help"},
      usageHelp = true,
      description = "Display this help message.")
  @SuppressWarnings("unused") // Assigned by the CLI framework
  boolean usageHelpRequested;

  @Option(
      names = {"-v", "--version"},
      versionHelp = true,
      description = "Print version information.")
  @SuppressWarnings("unused") // Assigned by the CLI framework
  boolean versionRequested;

  @Override
  public Integer call() throws Exception {
    long start = System.currentTimeMillis();
    if (dataSize == 0) {
      dataSize = DEFAULT_DATA_SIZE;
    }
    if (bufferSize == 0) {
      bufferSize = DEFAULT_BUFFER_SIZE;
    }
    Benchmark.Builder builder = createBuilderWithCandidates();
    builder
        .setDataSize(dataSize)
        .setBufferSize(bufferSize)
        .addResultConsumer(new CsvResultConsumer(System.out, /* withHeader= */ true));
    FileWriter fileWriter = null;
    if (outputCsvPath != null) {
      boolean isExistingFile = Files.exists(outputCsvPath);
      fileWriter = new FileWriter(outputCsvPath.toFile(), /* append= */ true);
      builder.addResultConsumer(
          new CsvResultConsumer(fileWriter, /* withHeader= */ !isExistingFile));
    }
    Benchmark benchmark = builder.build();
    try {
      benchmark.call();
      return 0;
    } catch (Exception e) {
      e.printStackTrace();
      return -1;
    } finally {
      long end = System.currentTimeMillis();
      System.out.println("Total time: " + (end - start) / 1000.0 + " seconds");
      if (fileWriter != null) {
        fileWriter.flush();
        fileWriter.close();
      }
    }
  }

  public static void main(String[] args) {
    CommandLine commandLine = new CommandLine(new Main());
    int exitCode = 0;
    if (commandLine.isUsageHelpRequested()) {
      commandLine.usage(System.out);
    } else if (commandLine.isVersionHelpRequested()) {
      commandLine.printVersionHelp(System.out);
    } else {
      exitCode = commandLine.execute(args);
    }
    System.exit(exitCode);
  }

  private static Benchmark.Builder createBuilderWithCandidates() {
    return Benchmark.newBuilder()
        // Guava
        .addCandidate(ProviderName.GUAVA, HashAlgorithm.ADLER32)
        .addCandidate(ProviderName.GUAVA, HashAlgorithm.CRC32)
        .addCandidate(ProviderName.GUAVA, HashAlgorithm.CRC32C)
        .addCandidate(ProviderName.GUAVA, HashAlgorithm.MD5)
        .addCandidate(ProviderName.GUAVA, HashAlgorithm.MURMUR3_32)
        .addCandidate(ProviderName.GUAVA, HashAlgorithm.MURMUR3_128)
        .addCandidate(ProviderName.GUAVA, HashAlgorithm.SHA1)
        .addCandidate(ProviderName.GUAVA, HashAlgorithm.SHA256)
        .addCandidate(ProviderName.GUAVA, HashAlgorithm.SHA384)
        .addCandidate(ProviderName.GUAVA, HashAlgorithm.SHA512)
        .addCandidate(ProviderName.GUAVA, HashAlgorithm.SIP_HASH_24)
        // Java
        .addCandidate(ProviderName.JAVA, HashAlgorithm.ADLER32)
        .addCandidate(ProviderName.JAVA, HashAlgorithm.CRC32)
        .addCandidate(ProviderName.JAVA, HashAlgorithm.CRC32C)
        .addCandidate(ProviderName.JAVA, HashAlgorithm.MD5)
        .addCandidate(ProviderName.JAVA, HashAlgorithm.SHA1)
        .addCandidate(ProviderName.JAVA, HashAlgorithm.SHA256)
        // Apache Commons Codec
        .addCandidate(ProviderName.APACHE, HashAlgorithm.MD2)
        .addCandidate(ProviderName.APACHE, HashAlgorithm.MD5)
        .addCandidate(ProviderName.APACHE, HashAlgorithm.SHA1)
        .addCandidate(ProviderName.APACHE, HashAlgorithm.SHA256)
        .addCandidate(ProviderName.APACHE, HashAlgorithm.SHA3_224)
        .addCandidate(ProviderName.APACHE, HashAlgorithm.SHA3_256)
        .addCandidate(ProviderName.APACHE, HashAlgorithm.SHA3_384)
        .addCandidate(ProviderName.APACHE, HashAlgorithm.SHA3_512)
        .addCandidate(ProviderName.APACHE, HashAlgorithm.SHA3_384)
        .addCandidate(ProviderName.APACHE, HashAlgorithm.SHA512_224)
        .addCandidate(ProviderName.APACHE, HashAlgorithm.SHA512_256)
        .addCandidate(ProviderName.APACHE, HashAlgorithm.SHA512)
        // jpountz.net
        .addCandidate(ProviderName.JPOUNTZ, HashAlgorithm.XXHASH32)
        .addCandidate(ProviderName.JPOUNTZ, HashAlgorithm.XXHASH64);
  }
}
