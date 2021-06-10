package com.quickshare.framework.environment.service;

import com.quickshare.common.bean.web.BaseResponse;
import com.quickshare.framework.environment.dao.ConfigureDao;
import com.quickshare.framework.environment.dto.GroupInfo;
import com.quickshare.framework.environment.dto.ProfileInfo;
import com.quickshare.framework.environment.dto.PropInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liu_ke
 */
@Service
public class ConfigureService {

    private final ConfigureDao configureDao;

    public ConfigureService(ConfigureDao configureDao){
        this.configureDao = configureDao;
    }

    public Object lstApps() throws Exception {
        return new BaseResponse<>(0,"查询成功",configureDao.lstApps());
    }

    public Object lstProfiles(String app) throws Exception {
        return new BaseResponse<>(0,"查询成功",configureDao.lstProfiles(app));
    }

    public Object addProfile(ProfileInfo profile) throws Exception {
        // todo 校验参数
        boolean result = configureDao.addProfile(profile);
        if(result){
            return new BaseResponse<>(0,String.format("添加[%s-%s]环境成功",profile.getId(),profile.getName()),true);
        }
        else{
            return new BaseResponse<>(1,String.format("添加[%s-%s]环境失败",profile.getId(),profile.getName()),false);
        }
    }

    public Object cloneProfile(ProfileInfo profile) throws Exception {
        // todo 校验参数
        boolean result = configureDao.cloneProfile(profile);
        if(result){
            return new BaseResponse<>(0,String.format("添加[%s-%s]环境成功",profile.getId(),profile.getName()),true);
        }
        else{
            return new BaseResponse<>(1,String.format("添加[%s-%s]环境失败",profile.getId(),profile.getName()),false);
        }
    }

    public Object deleteProfile(String app,String id,String name) throws Exception{
        boolean result = configureDao.deleteProfile(app,id);
        if(result){
            return new BaseResponse<>(0,String.format("删除[%s-%s]环境成功",id,name),true);
        }
        else{
            return new BaseResponse<>(1,String.format("删除[%s-%s]环境失败",id,name),false);
        }
    }

    public Object lstConfiguration(String app,String profile) throws Exception {
        List<GroupInfo> groups = configureDao.lstGroups(app);
        List<PropInfo> props = configureDao.lstConfiguration(app,profile);
        List<PropInfo> options = configureDao.lstOption(app);

        final List<GroupInfo> emptyGroups = new ArrayList<>();
        for (GroupInfo group:groups) {
            List<PropInfo> childProps = props.stream().filter(p->group.getId().equals(p.getGroupKey1())).collect(Collectors.toList());
            if(childProps.isEmpty()){
                emptyGroups.add(group);
                continue;
            }
            group.setProps(generatePropTree(childProps,options));
        }
        if(!emptyGroups.isEmpty()){
            groups.removeAll(emptyGroups);
        }

        return new BaseResponse<>(0,"查询成功",groups);
    }

    private List<PropInfo> generatePropTree(List<PropInfo> props,List<PropInfo> options){
        for (PropInfo prop:props) {
            List<PropInfo> childProps = props.stream().filter(p->prop.getKey1().equals(p.getParentKey1())).collect(Collectors.toList());
            if(!childProps.isEmpty()){
                prop.setChilds(childProps);
            }
            else {
                prop.setValue1(convertValue(prop.getStrValue1(),prop.getValueType(),prop.getIsArray()));
                if(!StringUtils.isEmpty(prop.getDependKey1())){
                    props.stream().filter(p->prop.getDependKey1().equals(p.getKey1())).findFirst().ifPresent(p->{
                        prop.setDependValue1(convertValue(prop.getStrDependValue1(),p.getValueType()));
                    });
                }
                if ("radiogroup".equals(prop.getInputType()) || "checkgroup".equals(prop.getInputType())) {
                    List<PropInfo> propOptions = options.stream().filter(p -> prop.getKey1().equals(p.getParentKey1())).collect(Collectors.toList());
                    if (!propOptions.isEmpty()) {
                        propOptions.forEach(p->p.setValue1(convertValue(p.getStrValue1(),prop.getValueType())));
                        prop.setOptions(propOptions);
                    }
                }
            }
        }
        props.removeIf(p-> !StringUtils.isEmpty(p.getParentKey1()));
        return props;
    }

    private Object convertValue(String strValue,String type,String isArray){
        try {
            if("1".equals(isArray)){
                strValue = strValue.replace("[","").replace("]","");
                String[] strValues = strValue.split(",");
                return Arrays.stream(strValues).map(p->convertValue(p,type)).toArray();
            }
            else{
                return convertValue(strValue,type);
            }
        }
        catch (Exception e){
            return strValue;
        }
    }

    private Object convertValue(String strValue,String type){
        try {
            switch (type) {
                case "int":
                    return Integer.parseInt(strValue);
                case "date":
                    return DateUtils.parseDate(strValue, "yyyy-MM-dd");
                case "time":
                    return DateUtils.parseDate(strValue, "HH:mm:ssHH:mm:ss");
                case "datetime":
                    return DateUtils.parseDate(strValue, "yyyy-MM-dd HH:mm:ss");
                case "number":
                    return Float.parseFloat(strValue);
                case "bool":
                    return Boolean.parseBoolean(strValue);
                default:
                    return strValue;
            }
        }
        catch (ParseException e) {
            return strValue;
        }
    }
}
