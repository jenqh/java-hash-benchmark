package ms.jen.hashing.benchmark.worker;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.hash.HashCode;
import java.util.zip.Checksum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JavaChecksumHashWorkerTest {

  private JavaChecksumHashWorker worker;

  @Mock private Checksum mockChecksum;

  @BeforeEach
  void setUp() {
    worker = new JavaChecksumHashWorker(mockChecksum);
  }

  @Test
  void update() {
    byte[] bytes = new byte[1];

    worker.update(bytes);

    verify(mockChecksum).update(bytes, 0, bytes.length);
  }

  @Test
  void getResult() {
    when(mockChecksum.getValue()).thenReturn(1L);

    HashCode hashCode = worker.getResult();

    assertThat(hashCode).isEqualTo(HashCode.fromInt(1));
  }
}
