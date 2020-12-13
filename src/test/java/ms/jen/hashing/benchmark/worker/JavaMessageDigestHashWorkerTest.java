package ms.jen.hashing.benchmark.worker;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.hash.HashCode;
import java.security.MessageDigest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JavaMessageDigestHashWorkerTest {

  private JavaMessageDigestHashWorker worker;

  @Mock private MessageDigest mockMessageDigest;

  @BeforeEach
  void setUp() {
    worker = new JavaMessageDigestHashWorker(mockMessageDigest);
  }

  @Test
  void update() {
    byte[] bytes = new byte[1];

    worker.update(bytes);

    verify(mockMessageDigest).update(bytes);
  }

  @Test
  void getResult() {
    byte[] resultBytes = new byte[1];
    when(mockMessageDigest.digest()).thenReturn(resultBytes);

    HashCode hashCode = worker.getResult();

    assertThat(hashCode).isEqualTo(HashCode.fromBytes(resultBytes));
  }
}
