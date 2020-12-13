package ms.jen.hashing.benchmark.worker;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GuavaHashWorkerTest {

  private GuavaHashWorker worker;

  @Mock private Hasher mockHasher;
  @Mock private HashFunction mockHashFunction;

  @BeforeEach
  void setUp() {
    when(mockHashFunction.newHasher()).thenReturn(mockHasher);
    worker = GuavaHashWorker.of(mockHashFunction);
  }

  @Test
  void update() {
    byte[] bytes = new byte[2];

    worker.update(bytes);

    verify(mockHasher).putBytes(bytes);
  }

  @Test
  void getResult() {
    HashCode hashCode = HashCode.fromInt(1);
    when(mockHasher.hash()).thenReturn(hashCode);

    HashCode result = worker.getResult();

    assertThat(result).isSameAs(hashCode);
  }
}
