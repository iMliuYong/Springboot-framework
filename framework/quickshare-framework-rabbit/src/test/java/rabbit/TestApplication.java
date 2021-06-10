package rabbit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author liu_ke
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.quickshare")
public class TestApplication {

    public static void main(String[] args){
        new SpringApplication(TestApplication.class).run(args);
    }
}
