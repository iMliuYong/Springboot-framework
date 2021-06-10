package com.quickshare.framework.environment.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

/**
 * 配置属性信息
 * @author liu_ke
 */
public class PropInfo {

    private String key1;

    private String label;

    private Double labelWidth;

    private String caption;

    private Double inputWidth;

    /**
     * 属性值，如果属性未配置，读取默认值
     */
    private Object value1;

    @JsonIgnore
    private String strValue1;

    @JsonIgnore
    private String isArray;

    /**
     * 项位置：follow/newline
     */
    private String location;

    private String hint;

    private String inputType;

    private String valueType;

    private String valueRule;

    private String valueRuleHint;

    private String dependKey1;

    private Object dependValue1;

    @JsonIgnore
    private String strDependValue1;

    private String groupKey1;

    private String parentKey1;

    /**
     * 项提示位置：follow/newline
     */
    private String hintLocation;

    /**
     * 子项
     */
    private List<PropInfo> childs;

    /**
     * 选项
     */
    private List<PropInfo> options;

    public String getKey1() {
        return key1;
    }

    public void setKey1(String key1) {
        this.key1 = key1;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Object getValue1() {
        return value1;
    }

    public void setValue1(Object value1) {
        this.value1 = value1;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public String getDependKey1() {
        return dependKey1;
    }

    public void setDependKey1(String dependKey1) {
        this.dependKey1 = dependKey1;
    }

    public String getStrValue1() {
        return strValue1;
    }

    public void setStrValue1(String strValue1) {
        this.strValue1 = strValue1;
    }

    public String getIsArray() {
        return isArray;
    }

    public void setIsArray(String isArray) {
        this.isArray = isArray;
    }

    public String getGroupKey1() {
        return groupKey1;
    }

    public void setGroupKey1(String groupKey1) {
        this.groupKey1 = groupKey1;
    }

    public String getParentKey1() {
        return parentKey1;
    }

    public void setParentKey1(String parentKey1) {
        this.parentKey1 = parentKey1;
    }

    public String getHintLocation() {
        return hintLocation;
    }

    public void setHintLocation(String hintLocation) {
        this.hintLocation = hintLocation;
    }

    public String getValueRule() {
        return valueRule;
    }

    public void setValueRule(String valueRule) {
        this.valueRule = valueRule;
    }

    public String getValueRuleHint() {
        return valueRuleHint;
    }

    public void setValueRuleHint(String valueRuleHint) {
        this.valueRuleHint = valueRuleHint;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Double getLabelWidth() {
        return labelWidth;
    }

    public void setLabelWidth(Double labelWidth) {
        this.labelWidth = labelWidth;
    }

    public Double getInputWidth() {
        return inputWidth;
    }

    public void setInputWidth(Double inputWidth) {
        this.inputWidth = inputWidth;
    }

    public Object getDependValue1() {
        return dependValue1;
    }

    public void setDependValue1(Object dependValue1) {
        this.dependValue1 = dependValue1;
    }

    public String getStrDependValue1() {
        return strDependValue1;
    }

    public void setStrDependValue1(String strDependValue1) {
        this.strDependValue1 = strDependValue1;
    }

    public List<PropInfo> getChilds() {
        return childs;
    }

    public void setChilds(List<PropInfo> childs) {
        this.childs = childs;
    }

    public List<PropInfo> getOptions() {
        return options;
    }

    public void setOptions(List<PropInfo> options) {
        this.options = options;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }
}
