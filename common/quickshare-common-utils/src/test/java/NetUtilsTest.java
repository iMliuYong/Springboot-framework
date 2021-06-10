import com.quickshare.common.utils.NetUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("NetUtils测试类")
public class NetUtilsTest {

    @Test
    @DisplayName("testLocalPort")
    public void testLocalPort() {

        boolean isLoclePortUsing = NetUtils.isLoclePortUsing(8080);
        System.out.print(isLoclePortUsing);
    }
}
