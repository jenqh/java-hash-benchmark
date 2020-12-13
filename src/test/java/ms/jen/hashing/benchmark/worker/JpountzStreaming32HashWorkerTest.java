package ms.jen.hashing.benchmark.worker;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.hash.HashCode;
import net.jpountz.xxhash.StreamingXXHash32;
import net.jpountz.xxhash.XXHashFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JpountzStreaming32HashWorkerTest {

  private JpountzStreaming32HashWorker worker;

  @Mock private XXHashFactory mockXXHashFactory;
  @Mock private StreamingXXHash32 mockStreamingXXHash32;

  @BeforeEach
  void setUp() {
    when(mockXXHashFactory.newStreamingHash32(0)).thenReturn(mockStreamingXXHash32);
    worker = new JpountzStreaming32HashWorker(mockXXHashFactory);
  }

  @Test
  void update() {
    byte[] bytes = new byte[1];

    worker.update(bytes);

    verify(mockStreamingXXHash32).update(bytes, 0, bytes.length);
  }

  @Test
  void getResult() {
    when(mockStreamingXXHash32.getValue()).thenReturn(1);

    HashCode hashCode = worker.getResult();

    assertThat(hashCode).isEqualTo(HashCode.fromInt(1));
  }
}
