package com.quickshare.framework.environment.dao;

import com.quickshare.framework.datasource.DataSourceUtils;
import com.quickshare.framework.environment.dto.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;
import java.util.Date;
import java.util.List;

/**
 * @author liu_ke
 */
@Repository
public class ConfigureDao {

    private final Environment environment;

    private final String SQL_QUERY_APP="select APPLICATION,NAME1 from SYS_CONFIG_APP a where JLZT='1' " +
            "and exists(select 1 from SYS_CONFIG_RULE b where b.APPLICATION = a.APPLICATION and b.JLZT='1')";

    private final String SQL_QUERY_PROFILE="select PROFILE,NAME1 from SYS_CONFIG_PROFILE where APPLICATION=? and JLZT='1'";

    private final String SQL_QUERY_GROUP="select KEY1,NAME1 from SYS_CONFIG_GROUP where APPLICATION=? ORDER BY SEQ";

    private final String SQL_ADD_PROFILE="insert into SYS_CONFIG_PROFILE ( APPLICATION, PROFILE, NAME1, JLZT ) values (?,?,?,'1')";

    private final String SQL_QUERY_PROP="select a.KEY1,a.LABEL,a.LABEL_WIDTH as labelWidth,a.CAPTION,b.VALUE1,a.LOCATION,a.HINT,\n" +
            "  a.VALUE_TYPE as valueType,a.VALUE_RULE as valueRule,a.VALUE_RULE_HINT as valueRuleHint,a.INPUT_TYPE as inputType,\n" +
            "  a.DEPEND_KEY1 as dependKey1,a.DEPEND_VALUE1 as strDependValue1,a.IS_ARRAY as isArray,\n" +
            "  a.GROUP_KEY1 as groupKey1,a.PARENT_KEY1 as parentKey1,a.HINT_LOCATION as hintLocation\n" +
            "from SYS_CONFIG_RULE a\n" +
            "left join SYS_CONFIG_PROPERTIES b on a.APPLICATION=b.APPLICATION and a.KEY1=b.KEY1 and b.PROFILE=?\n" +
            "where a.APPLICATION=? and a.JLZT='1'\n" +
            "order by a.SEQ";

    private final String SQL_QUERY_PROP_OPTION="select VALUE1 as parentKey1,OPTION_VALUE as strValue1,LOCATION,CAPTION as label,HINT,HINT_LOCATION as hintLocation  \n" +
            "from SYS_CONFIG_RULE_OPTION \n" +
            "where APPLICATION=? and JLZT='1'\n" +
            "order by SEQ";

    private final String SQL_ADD_PROP_DEFAULT="insert into SYS_CONFIG_PROPERTIES ( APPLICATION, KEY1, VALUE1, PROFILE, GXSJ )\n" +
            "select APPLICATION,KEY1,DEFAULT_VALUE,?,?\n" +
            "from SYS_CONFIG_RULE\n" +
            "where APPLICATION=? and DEFAULT_VALUE is not null";

    private final String SQL_ADD_PROP_CLONE="insert into SYS_CONFIG_PROPERTIES ( APPLICATION, KEY1, VALUE1, PROFILE, GXSJ )\n" +
            "select APPLICATION,KEY1,VALUE1,?,?\n" +
            "from SYS_CONFIG_PROPERTIES\n" +
            "where APPLICATION=? and PROFILE=?";

    private final String SQL_DELETE_PROFILE="delete from SYS_CONFIG_PROFILE where APPLICATION=? and PROFILE=?";

    private final String SQL_DELETE_PROPERTY="delete from SYS_CONFIG_PROPERTIES where APPLICATION=? and PROFILE=?";

    private final String SQL_ADD_PROP_HISTORY_BY_PROFILE="insert into SYS_CONFIG_PROPERTIES_HISTORY ( APPLICATION, KEY1, VALUE1, PROFILE, LABEL, GXSJ )\n" +
            "select APPLICATION,KEY1,VALUE1,PROFILE,LABEL,?\n" +
            "from SYS_CONFIG_PROPERTIES\n" +
            "where APPLICATION=? and PROFILE=?";

    private JdbcTemplate jdbcTemplate;
    private DataSourceTransactionManager transactionManager;

    public ConfigureDao(Environment environment){
        this.environment = environment;
    }

    public JdbcTemplate getJdbcTemplate() throws Exception {
        if(jdbcTemplate != null){
            return jdbcTemplate;
        }

        String url = environment.getProperty("spring.profiles.datasource.url");
        String username = environment.getProperty("spring.profiles.datasource.username");
        String password = environment.getProperty("spring.profiles.datasource.password");
        String driverClassName = environment.getProperty("spring.profiles.datasource.driver-class-name");
        if(StringUtils.isEmpty(url)){
            throw new Exception("未设置配置数据库信息");
        }
        DataSource dataSource = DataSourceUtils.createDataSource(url,username,password,driverClassName);
        jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate;
    }

    public TransactionStatus beginTran() throws Exception {
        if(transactionManager == null){
            transactionManager = new DataSourceTransactionManager(getJdbcTemplate().getDataSource());
        }
        //事务定义类
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);
        return status;
    }

    public List<AppInfo> lstApps() throws Exception{
        return getJdbcTemplate().query(SQL_QUERY_APP,new Object[]{}, (rs, rowNum) -> {
            AppInfo app = new AppInfo();
            app.setId(rs.getString("APPLICATION"));
            app.setName(rs.getString("NAME1"));
            return app;
        });
    }

    public List<ProfileInfo> lstProfiles(String app) throws Exception {
        return getJdbcTemplate().query(SQL_QUERY_PROFILE,new Object[]{app}, (rs, rowNum) -> {
            ProfileInfo profileInfo = new ProfileInfo();
            profileInfo.setId(rs.getString("PROFILE"));
            profileInfo.setName(rs.getString("NAME1"));
            return profileInfo;
        });
    }

    public boolean addProfile(ProfileInfo profile) throws Exception {
        Object[] param = new Object[]{profile.getApp(),profile.getId(),profile.getName()};
        TransactionStatus status = beginTran();
        try{
            int cnt = getJdbcTemplate().update(SQL_ADD_PROFILE,param);
            if(cnt > 0){
                param = new Object[]{profile.getId(),new Date(System.currentTimeMillis()),profile.getApp()};
                getJdbcTemplate().update(SQL_ADD_PROP_DEFAULT,param);
                transactionManager.commit(status);
                return true;
            }
            else{
                return false;
            }
        }
        catch (Exception ex){
            transactionManager.rollback(status);
            return false;
        }
    }

    public boolean cloneProfile(ProfileInfo profile) throws Exception {
        Object[] param = new Object[]{profile.getApp(),profile.getId(),profile.getName()};
        TransactionStatus status = beginTran();
        try{
            int cnt = getJdbcTemplate().update(SQL_ADD_PROFILE,param);
            if(cnt > 0){
                param = new Object[]{profile.getId(),new Date(System.currentTimeMillis()),profile.getApp(),profile.getSourceId()};
                getJdbcTemplate().update(SQL_ADD_PROP_CLONE,param);
                transactionManager.commit(status);
                return true;
            }
            else{
                return false;
            }
        }
        catch (Exception ex){
            transactionManager.rollback(status);
            return false;
        }
    }

    public boolean deleteProfile(String app,String profile) throws Exception {
        TransactionStatus status = beginTran();
        JdbcTemplate jdbcTemplate = getJdbcTemplate();
        try{
            jdbcTemplate.update(SQL_ADD_PROP_HISTORY_BY_PROFILE,new Object[]{new Date(System.currentTimeMillis()),app,profile});
            jdbcTemplate.update(SQL_DELETE_PROPERTY,new Object[]{app,profile});
            jdbcTemplate.update(SQL_DELETE_PROFILE,new Object[]{app,profile});
            transactionManager.commit(status);
            return true;
        }
        catch (Exception ex){
            transactionManager.rollback(status);
            return false;
        }
    }

    public List<GroupInfo> lstGroups(String app) throws Exception {
        return getJdbcTemplate().query(SQL_QUERY_GROUP,new Object[]{app}, (rs, rowNum) -> {
            GroupInfo groupInfo = new GroupInfo();
            groupInfo.setId(rs.getString("KEY1"));
            groupInfo.setName(rs.getString("NAME1"));
            return groupInfo;
        });
    }

    public List<PropInfo> lstConfiguration(String app,String profile) throws Exception {
        return getJdbcTemplate().query(SQL_QUERY_PROP,new Object[]{profile,app}, new BeanPropertyRowMapper<>(PropInfo.class));
    }

    public List<PropInfo> lstOption(String app) throws Exception {
        return getJdbcTemplate().query(SQL_QUERY_PROP_OPTION,new Object[]{app}, new BeanPropertyRowMapper<>(PropInfo.class));
    }
}
