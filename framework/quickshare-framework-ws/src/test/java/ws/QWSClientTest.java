package ws;

import com.quickshare.framework.ws.PasswordType;
import com.quickshare.framework.ws.WebServiceClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={TestApplication.class})
public class QWSClientTest {

    @Test
    public void test2(){
        WebServiceClient wsClient = new WebServiceClient("http://127.0.0.1:19008/ws/test2?wsdl",true,"user1","557yx9h/efc=", PasswordType.DIGEST);
        try {
            String result = wsClient.invoke("sayHello", "zhengmo");
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test(){
        WebServiceClient wsClient = new WebServiceClient("http://127.0.0.1:19008/ws/test?wsdl");
        try {
            String result = wsClient.invoke("HIPMessageServer", "zhengmo");
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
