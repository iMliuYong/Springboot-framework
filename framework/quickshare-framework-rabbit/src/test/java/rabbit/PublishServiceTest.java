package rabbit;

import com.quickshare.framework.rabbit.service.PublishService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={TestApplication.class})
public class PublishServiceTest {

    @Autowired
    private PublishService publishService;

    @Test
    public void publishWithTran() throws IOException {
        byte[] message = "国庆节快乐1！".getBytes("utf-8");
        Map<String,byte[]> messages = new HashMap<>();
        messages.put("aaa",message);
        messages.put("bbbb",message);
        messages.put("ccc",message);
        publishService.publish("aaa",messages);
    }
}
