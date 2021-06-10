package com.quickshare.common.messagecode.consts;

import org.omg.CORBA.PUBLIC_MEMBER;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息码(8位)
 * 0:成功
 * 1*******:系统级,SYS_***
 * 2*******:数据库相关,DB_***
 * 3*******:登录相关,LOGIN_***
 * 4*******:业务消息,有业务系统中进行定义
 * @author liu_ke
 */
public class MessageCode {

    public static Map<Integer,String> VALUES=new HashMap<>();

    public static final Integer CODE_SYS_UNKNOWN=10000000;

    public static final String MESSAGE_SYS_UNKNOWN_CODE="未知的信息码";
}
