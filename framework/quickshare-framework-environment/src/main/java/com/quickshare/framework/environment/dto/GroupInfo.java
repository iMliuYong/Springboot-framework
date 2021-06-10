package com.quickshare.framework.environment.dto;

import java.util.List;

/**
 * @author liu_ke
 */
public class GroupInfo {

    private String id;

    private String name;

    private List<PropInfo> props;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PropInfo> getProps() {
        return props;
    }

    public void setProps(List<PropInfo> props) {
        this.props = props;
    }
}
