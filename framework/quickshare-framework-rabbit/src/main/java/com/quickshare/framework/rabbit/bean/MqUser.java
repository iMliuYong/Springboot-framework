package com.quickshare.framework.rabbit.bean;

/**
 * @author liu_ke
 */
public class MqUser {

    private String name;

    private String tag;

    public MqUser(){}

    public MqUser(String name,String tag){
        this.name = name;
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
