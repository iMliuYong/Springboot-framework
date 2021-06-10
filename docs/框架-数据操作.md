
# 框架-数据操作

## mybatis相关

### 加载Mapper

通过添加一个配置文件，`Configuration`,添加`MapperScan`扫描相关package下面的Mapper接口。

```java
@Configuration
@MapperScan("com.quickshare.samples.datasource.mapper.**")
public class AppConfig {
}
```

### model使用

mybatis能扫描指定的package下的类进行生产别名(其实就是短类名)，然后再xml文件中使用。这个对注解方式书写的sql语句没有影响。

目前默认扫描的包为：com.quickshare.**.model,com.quickshare.**.domain,com.quickshare.**.model.**,com.quickshare.**.domain.**。

```xml
<select id="query" resultType="com.quickshare.samples.datasource.bean.TestTable">
    select idx,col1,convert(varchar(20),opTime,120) as opTime from testTable
</select>
```

```java
@Mapper
public interface TestMapper {
    @Insert("<script>insert into testTable ( col1 ) values(#{col1})</script>")
    int add(@Param("col1") String col1);
}
```

### xml使用

默认扫描的xml地址为：mapper/**/*.xml。

### 多种数据库支持

通过`databaseId`进行不同数据库区分。目前支持：oracle，mysql，sqlserver。

```xml
<mapper>
    <select id="query" resultType="com.quickshare.samples.datasource.bean.TestTable" databaseId="mysql">
        select idx,col1,convert(varchar(20),opTime,120) as opTime from testTable
    </select>
    
    <select id="query" resultType="com.quickshare.samples.datasource.bean.TestTable" databaseId="sqlserver">
        select idx,col1,convert(varchar(20),opTime,120) as opTime from testTable
    </select>
</mapper>
```

## 多数据源使用

说明，多数据源目前不支持跨数据库事务。多数据源包括两种情况，一种是应用需要连接多个数据库，一种是分库。

程序中主要使用的注解，`com.quickshare.framework.datasource.DataSource`，可以注解在方法上，也可以注解在类上，方法上的注解优先使用。

### 多数据库

多数据源是程序连接多个数据库，譬如目前一个应用，后端有多个数据库进行数据存储。

配置时，读取spring.datasource作为默认数据源；app节点用于配置子应用，如下，db1和db2是两个子应用，其下的datasource为子应用的数据源配置。

#### 配置

```yml
spring:
  datasource:
    url: jdbc:sqlserver://127.0.0.1\MYMSSQL;databaseName=dep
    username: sa
    password: abc@321
    driver-class-name: com.microsoft.sqlserver.jdbc.SQLServerDriver
app:
  db1:
    datasource:
      url: jdbc:sqlserver://127.0.0.1\MYMSSQL;databaseName=testdb1
      username: sa
      password: abc@321
      driver-class-name: com.microsoft.sqlserver.jdbc.SQLServerDriver
  db2:
    datasource:
      url: jdbc:sqlserver://127.0.0.1\MYMSSQL;databaseName=testdb2
      username: sa
      password: abc@321
      driver-class-name: com.microsoft.sqlserver.jdbc.SQLServerDriver
```

#### 编码

```java
@Service
public class TestMultiService {
    @DataSource("db1")
    public Object query1(){
        return testMapper.query();
    }
    
    @DataSource("db2")
    public Object query2(){
        return testMapper.query();
    }
}
```

### 分库

分库基于默认分库规则配置与分库数据源配置。

```sql
create table SYS_SHARDING_DB_MAPPING (
    ID                   varchar(36)          not null,  -- 主键
    SYS_ID               varchar(20)          not null,  -- 应用ID
    SYS_NAME             varchar(36)          not null,  -- 应用名称
    SPLIT_KEY            varchar(36)          not null,  -- 分库键
    MASTER_URL           varchar(2000)        not null,  -- 主数据库连接url
    MASTER_IP            varchar(128)         not null,  -- 主数据库IP地址
    MASTER_PORT          varchar(6)           not null,  -- 主数据库端口
    MASTER_DBNAME        varchar(12)          not null,  -- 主数据库名
    MASTER_USER          varchar(40)          not null,  -- 主数据库用户名
    MASTER_PWD           varchar(128)         not null,  -- 主数据库密码
    SLAVE_URL            varchar(2000)        null,      -- 从数据库连接url
    SLAVE_IP             varchar(128)         null,      -- 从数据库IP地址
    SLAVE_PORT           varchar(6)           null,      -- 从数据库端口
    SLAVE_DBNAME         varchar(12)          null,      -- 从数据库名
    SLAVE_USER           varchar(40)          null,      -- 从数据库用户名
    SLAVE_PWD            varchar(128)         null,      -- 从数据库密码
    DRIVER               varchar(100)         not null,  -- 数据库驱动
    JLZT                 char(1)              not null,  -- 是否有效 1启用 0禁用
    constraint PK_SYS_SHARDING_DB_MAPPING primary key (ID),
    constraint IX_SYS_SHARDING_DB_MAPPING unique nonclustered (SYS_ID,SPLIT_KEY)
)
```

#### 配置

数据示例：

| ID | SYS_ID | SPLIT_KEY | MASTER_URL | MASTER_USER | MASTER_PWD | DRIVER | JLZT |
|---|---|---|---|----|----|----|----|
|id1|dbsample|test1|jdbc:sqlserver://127.0.0.1\MYMSSQL;databaseName=testdb1|sa|abc@321|com.microsoft.sqlserver.jdbc.SQLServerDriver|1
|id2|dbsample|test2|jdbc:sqlserver://127.0.0.1\MYMSSQL;databaseName=testdb2|sa|abc@321|com.microsoft.sqlserver.jdbc.SQLServerDriver|1

```yml
app:
  dbsample: # 子应用名称，对应 SYS_DATABASE.NAME
    dbsplit: # 分库信息
      datasource: # 分库数据表所在数据库的连接信息
        url: jdbc:sqlserver://127.0.0.1\MYMSSQL;databaseName=dep
        username: sa
        password: abc@321
        driver-class-name: com.microsoft.sqlserver.jdbc.SQLServerDriver
      accord: # 分库依据信息
        type: com.quickshare.samples.datasource.bean.User # 类，必须被Spring进行bean管理
        method: getCustomerId # 上面类的一个方法，获取分库路由信息，对应 SYS_DATABASE.SPLIT_KEY
```

#### 编码

以下代码示例，3个方法的数据库操作都会根据分库依据进行不同的数据库连接操作。

```java
/**
 * 注解在方法上的方式
 */
@Service
public class TestOnMethodService {

    private final TestMapper testMapper;

    public TestOnMethodService(TestMapper testMapper){
        this.testMapper = testMapper;
    }

    @DataSource("dbsample")
    public Object add(String col1){
        testMapper.add(col1);
        return "保存成功";
    }

    @DataSource("dbsample")
    public Object query(){
        return testMapper.query();
    }

    @DataSource("dbsample")
    @Transactional(rollbackFor = Exception.class)
    public Object add2(String col1) throws Exception{
        testMapper.add(col1);
        throw new Exception("报错事务回滚。");
    }
}
```

```java
/**
 * 注解在类上的方式
 */
@Service
@DataSource("dbsample")
public class TestOnClassService {

    private final TestMapper testMapper;

    public TestOnClassService(TestMapper testMapper){
        this.testMapper = testMapper;
    }

    public Object add(String col1){
        testMapper.add(col1);
        return "保存成功";
    }

    public Object query(){
        return testMapper.query();
    }

    @Transactional(rollbackFor = Exception.class)
    public Object add2(String col1) throws Exception{
        testMapper.add(col1);
        throw new Exception("报错事务回滚。");
    }
}
```