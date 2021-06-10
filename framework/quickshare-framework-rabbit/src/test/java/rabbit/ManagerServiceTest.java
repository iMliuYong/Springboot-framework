package rabbit;

import com.quickshare.framework.rabbit.service.HostManagerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={TestApplication.class})
public class ManagerServiceTest {

    @Autowired
    private HostManagerService managerService;

    @Test
    public void lstVHosts(){
        List<String> vhost = managerService.lstVHost();
        System.out.print(vhost);
    }

    @Test
    public void addVHost(){
        managerService.createVHost("abc");
    }

    @Test
    public void lstUsers(){
        managerService.lstUsers();
    }

    @Test
    public void addUser(){
        managerService.addUser("aaa","bbb","abc");
    }

    @Test
    public void bindUser(){
        managerService.bindUser("aaa","abc");
    }

    @Test
    public void createExchange(){
        managerService.createExchange("abc","aaa");
    }

    @Test
    public void createQueue(){
        managerService.createQueue("abc","aaa");
    }

    @Test
    public void createDelayQueue(){
        managerService.createQueue("abc","bbb_delay","aaa",10,"bbb");
    }

    @Test
    public void bindingQueue(){
        managerService.bindingQueue("abc","aaa","aaa");
    }

    @Test
    public void bindingDelayQueue(){
        managerService.bindingQueue("abc","aaa","bbb_delay","bbb_delay");
    }
}
