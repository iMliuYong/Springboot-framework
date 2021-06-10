import com.quickshare.common.utils.CmdUtil;
import com.quickshare.common.utils.ExecResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

@DisplayName("CmdUtil测试类")
public class CmdTest {

    @Test
    @DisplayName("执行Cmd")
    public void runCmd() {
        String rabbitPath = "D:\\Program Files\\RabbitMQ Server\\rabbitmq_server-3.6.9\\sbin\\rabbitmqctl.bat";
        String[] cmdarray = new String[]{rabbitPath,"add_vhost","aaa"};
        String[] params = new String[]{"ERLANG_HOME=D:\\Program Files\\erl8.1",
            "Path="+System.getProperty("path")};
        ExecResult result = CmdUtil.executeLocalCmd(cmdarray,params);
        System.out.println(result);
    }
}
