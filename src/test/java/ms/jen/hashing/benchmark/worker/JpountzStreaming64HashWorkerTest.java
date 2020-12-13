package ms.jen.hashing.benchmark.worker;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.hash.HashCode;
import net.jpountz.xxhash.StreamingXXHash64;
import net.jpountz.xxhash.XXHashFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JpountzStreaming64HashWorkerTest {

  private JpountzStreaming64HashWorker worker;

  @Mock private XXHashFactory mockXXHashFactory;
  @Mock private StreamingXXHash64 mockStreamingXXHash64;

  @BeforeEach
  void setUp() {
    when(mockXXHashFactory.newStreamingHash64(0L)).thenReturn(mockStreamingXXHash64);
    worker = new JpountzStreaming64HashWorker(mockXXHashFactory);
  }

  @Test
  void update() {
    byte[] bytes = new byte[1];

    worker.update(bytes);

    verify(mockStreamingXXHash64).update(bytes, 0, bytes.length);
  }

  @Test
  void getResult() {
    when(mockStreamingXXHash64.getValue()).thenReturn(1L);

    HashCode hashCode = worker.getResult();

    assertThat(hashCode).isEqualTo(HashCode.fromLong(1));
  }
}
