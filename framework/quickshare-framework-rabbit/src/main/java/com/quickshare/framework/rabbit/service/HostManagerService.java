package com.quickshare.framework.rabbit.service;

import com.quickshare.common.utils.CmdUtil;
import com.quickshare.common.utils.ExecResult;
import com.quickshare.framework.rabbit.bean.MqUser;
import com.quickshare.framework.rabbit.core.Const;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author liu_ke
 */
@Service
@ConditionalOnProperty(name = "spring.rabbitmq.host")
public class HostManagerService {

    private final String CTL="rabbitmqctl.bat";

    @Value("${spring.rabbitmq.erl_home:}")
    private String erl_home;
    @Value("${spring.rabbitmq.rabbit_home:}")
    private String rabbit_home;

    private String rabbit_sbin;
    private String rabbit_ctl;

    public List<String> lstVHost(){
        String rabbit_ctl = getRabbit_ctl();
        String[] cmdarray = new String[]{rabbit_ctl,"list_vhosts"};
        String[] params = new String[]{"ERLANG_HOME="+getErl_home(), "Path="+System.getProperty("path")};
        ExecResult result = CmdUtil.executeLocalCmd(cmdarray,params);
        if(!StringUtils.isEmpty(result.getError())){
            throw new RuntimeException(result.getError());
        }
        List<String> vhosts = new ArrayList<>();
        if(result.getOutput().isEmpty()){
            return vhosts;
        }
        vhosts = result.getOutput().stream().skip(1).collect(Collectors.toList());
        return vhosts;
    }

    public void createVHost(String vhost){
        String rabbit_ctl = getRabbit_ctl();
        String[] cmdarray = new String[]{rabbit_ctl,"add_vhost",vhost};
        String[] params = new String[]{"ERLANG_HOME="+getErl_home(), "Path="+System.getProperty("path")};
        ExecResult result = CmdUtil.executeLocalCmd(cmdarray,params);
        if(!StringUtils.isEmpty(result.getError())){
            String errorMsg = result.getError();
            if(errorMsg.startsWith(Const.ERR_VHOST_EXISTS)){
                throw new RuntimeException(String.format("虚拟机[%s]已存在。",vhost));
            }
            else {
                throw new RuntimeException(errorMsg);
            }
        }
    }

    public List<MqUser> lstUsers(){
        String rabbit_ctl = getRabbit_ctl();
        String[] cmdarray = new String[]{rabbit_ctl,"list_users"};
        String[] params = new String[]{"ERLANG_HOME="+getErl_home(), "Path="+System.getProperty("path")};
        ExecResult result = CmdUtil.executeLocalCmd(cmdarray,params);
        if(!StringUtils.isEmpty(result.getError())){
            throw new RuntimeException(result.getError());
        }
        List<MqUser> users = new ArrayList<>();
        if(result.getOutput().isEmpty()){
            return users;
        }
        for (String strUser: result.getOutput().stream().skip(1).collect(Collectors.toList())) {
            String[] strUsers = strUser.split("\t");
            String tag = strUsers[1].substring(1,strUsers[1].length()-1);
            users.add(new MqUser(strUsers[0],tag));
        }
        return users;
    }

    public void addUser(String user,String pwd,String vhost){
        String rabbit_ctl = getRabbit_ctl();
        String[] cmdarray = new String[]{rabbit_ctl,"add_user",user,pwd};
        String[] params = new String[]{"ERLANG_HOME="+getErl_home(), "Path="+System.getProperty("path")};
        ExecResult result = CmdUtil.executeLocalCmd(cmdarray,params);
        if(!StringUtils.isEmpty(result.getError())){
            String errorMsg = result.getError();
            if(errorMsg.startsWith(Const.ERR_USER_EXISTS)){
                throw new RuntimeException(String.format("用户[%s]已存在。",user));
            }
            else {
                throw new RuntimeException(errorMsg);
            }
        }
        bindUser(user,vhost);
    }

    public void bindUser(String user,String vhost){
        String rabbit_ctl = getRabbit_ctl();
        String[] cmdarray = new String[]{rabbit_ctl,"set_permissions","-p",vhost,user,".*",".*",".*"};
        String[] params = new String[]{"ERLANG_HOME="+getErl_home(), "Path="+System.getProperty("path")};
        ExecResult result = CmdUtil.executeLocalCmd(cmdarray,params);
        if(!StringUtils.isEmpty(result.getError())){
            String errorMsg = result.getError();
            if(errorMsg.startsWith(Const.ERR_NOSUCH_VHOST)){
                throw new RuntimeException(String.format("虚拟机[%s]不存在。",vhost));
            }
            else {
                throw new RuntimeException(errorMsg);
            }
        }
    }

    public void createExchange(String vhost,String exchange){
        String rabbit_ctl = getRabbit_ctl();
        String[] cmdarray = new String[]{rabbit_ctl,"eval",String.format(Const.CREATE_EXCHANGE,vhost,exchange,"direct")};
        String[] params = new String[]{"ERLANG_HOME="+getErl_home(), "Path="+System.getProperty("path")};
        ExecResult result = CmdUtil.executeLocalCmd(cmdarray,params);
        if(!StringUtils.isEmpty(result.getError())){
            throw new RuntimeException(result.getError());
        }
    }

    public void createQueue(String vhost,String queue){
        String rabbit_ctl = getRabbit_ctl();
        String[] cmdarray = new String[]{rabbit_ctl,"eval",String.format(Const.CREATE_QUEUE,vhost,queue)};
        String[] params = new String[]{"ERLANG_HOME="+getErl_home(), "Path="+System.getProperty("path")};
        ExecResult result = CmdUtil.executeLocalCmd(cmdarray,params);
        if(!StringUtils.isEmpty(result.getError())){
            throw new RuntimeException(result.getError());
        }
    }

    public void createQueue(String vhost,String queue,String deadExchange,int delaySeconds,String routingKey){
        String rabbit_ctl = getRabbit_ctl();
        String[] cmdarray = new String[]{rabbit_ctl,"eval",String.format(Const.CREATE_QUEUE_DELAY,vhost,queue,deadExchange,delaySeconds*1000,routingKey)};
        String[] params = new String[]{"ERLANG_HOME="+getErl_home(), "Path="+System.getProperty("path")};
        ExecResult result = CmdUtil.executeLocalCmd(cmdarray,params);
        if(!StringUtils.isEmpty(result.getError())){
            throw new RuntimeException(result.getError());
        }
    }

    public void bindingQueue(String vhost,String exchange,String queue,String routingKey){
        String rabbit_ctl = getRabbit_ctl();
        String[] cmdarray = new String[]{rabbit_ctl,"eval",String.format(Const.BINDING_QUEUE,vhost,exchange,routingKey,vhost,queue)};
        String[] params = new String[]{"ERLANG_HOME="+getErl_home(), "Path="+System.getProperty("path")};
        ExecResult result = CmdUtil.executeLocalCmd(cmdarray,params);
        if(!StringUtils.isEmpty(result.getError())){
            throw new RuntimeException(result.getError());
        }
    }

    public void bindingQueue(String vhost,String exchange,String queue){
        bindingQueue(vhost,exchange,queue,queue);
    }

    private String getErl_home(){
        if(StringUtils.isEmpty(erl_home)){
            throw new RuntimeException("请先配置[spring.rabbitmq.erl_home]。");
        }
        File file = new File(erl_home);
        if(!file.exists()){
            throw new RuntimeException(String.format("配置[spring.rabbitmq.erl_home]设置的路径[%s]不存在。",erl_home));
        }
        return erl_home;
    }

    private String getRabbit_sbin(){
        if(!StringUtils.isEmpty(rabbit_sbin)){
            return rabbit_sbin;
        }
        if(StringUtils.isEmpty(rabbit_home)){
            throw new RuntimeException("请先配置[spring.rabbitmq.rabbit_home]。");
        }
        File file = new File(rabbit_home);
        if(!file.exists()){
            throw new RuntimeException(String.format("配置[spring.rabbitmq.rabbit_home]设置的路径[%s]不存在。",rabbit_home));
        }
        Optional<File> folder = Arrays.stream(file.listFiles()).filter(p->p.getName().startsWith("rabbitmq_server")).findAny();
        if(folder.isPresent()){
            rabbit_sbin = Paths.get(folder.get().getAbsolutePath(),"sbin").toString();
            return rabbit_sbin;
        }
        else{
            throw new RuntimeException("配置[spring.rabbitmq.rabbit_home]设置的路径[%s]不是有效的RabbitMQ安装目录。");
        }
    }

    private String getRabbit_ctl(){
        if(!StringUtils.isEmpty(rabbit_ctl)){
            return rabbit_ctl;
        }
        String ctl = Paths.get(getRabbit_sbin(),CTL).toString();
        File ctlFile = new File(ctl);
        if(!ctlFile.exists()){
            throw new RuntimeException(String.format("rabbitmqctl路径[%s]异常。",ctl));
        }
        rabbit_ctl = ctl;
        return rabbit_ctl;
    }
}
