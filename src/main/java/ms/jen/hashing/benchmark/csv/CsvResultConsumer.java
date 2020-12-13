package ms.jen.hashing.benchmark.csv;

import com.google.common.collect.ImmutableList;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;
import ms.jen.hashing.benchmark.core.Result;
import ms.jen.hashing.benchmark.util.StringUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

/** A {@link Consumer<Result>} class that converts {@link Result} to CSV format. */
public class CsvResultConsumer implements Consumer<Result> {

  public static final ImmutableList<String> CSV_HEADERS =
      ImmutableList.of(
          "Provider",
          "Algorithm",
          "Data Size",
          "Buffer Size",
          "Duration (ms)",
          "Speed (MB/s)",
          "Hash Size (bits)",
          "Hash Result");

  private final CSVPrinter printer;

  public CsvResultConsumer(Appendable appendable, boolean withHeader) {
    CSVFormat format = CSVFormat.DEFAULT;
    if (withHeader) {
      format = format.withHeader(CSV_HEADERS.toArray(new String[0]));
    }
    try {
      printer = new CSVPrinter(appendable, format);
    } catch (IOException e) {
      // Will be handled by the CLI framework
      throw new RuntimeException(e);
    }
  }

  @Override
  public void accept(Result result) {
    try {
      printer.printRecord(toCsvRow(result));
    } catch (IOException e) {
      // Will be handled by the CLI framework
      throw new RuntimeException(e);
    }
  }

  private static List<String> toCsvRow(Result result) {
    String hashString = result.hashCode.toString();
    return ImmutableList.of(
        result.provider.name(),
        result.algorithm.name(),
        StringUtils.humanReadableBytesSI(result.dataSize),
        Integer.toString(result.bufferSize),
        Long.toString(result.durationMillis),
        String.format("%.3f", result.mbPerSecond),
        Integer.toString(hashString.length() * 4),
        hashString);
  }
}
