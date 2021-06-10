
# maven编译


## 插件maven-dependency-plugin

依赖包管理插件，可以将依赖包生成到指定文件夹。

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-dependency-plugin</artifactId>
    <version>${build.plugin.dependency.version}</version>
    <executions>
        <execution>
            <id>copy-dependencies</id>
            <phase>package</phase>
            <goals>
                <goal>copy-dependencies</goal>
            </goals>
            <configuration>
                <outputDirectory>../apps/${sample.name}/lib</outputDirectory>
                <stripVersion>true</stripVersion>
            </configuration>
        </execution>
    </executions>
</plugin>
```

## 插件maven-resources-plugin

管理资源文件的插件，包括配置文件，批处理文件等。

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-resources-plugin</artifactId>
    <version>${build.plugin.resources.version}</version>
    <executions>
        <execution>
            <id>copy-resources</id>
            <!-- here the phase you need -->
            <phase>package</phase>
            <goals>
                <goal>copy-resources</goal>
            </goals>
            <configuration>
                <outputDirectory>../apps/${sample.name}/config</outputDirectory>
                <resources>
                    <resource>
                        <directory>src/main/resources</directory>
                        <includes>
                            <include>*.*</include>
                        </includes>
                        <filtering>true</filtering>
                    </resource>
                </resources>
                <encoding>UTF-8</encoding>
            </configuration>
        </execution>
        <execution>
            <id>copy-bat</id>
            <!-- here the phase you need -->
            <phase>package</phase>
            <goals>
                <goal>copy-resources</goal>
            </goals>
            <configuration>
                <outputDirectory>../apps/${sample.name}</outputDirectory>
                <resources>
                    <resource>
                        <directory>/</directory>
                        <includes>
                            <include>*.bat</include>
                        </includes>
                        <filtering>true</filtering>
                    </resource>
                </resources>
                <encoding>UTF-8</encoding>
            </configuration>
        </execution>
    </executions>
</plugin>
```

## 插件maven-antrun-plugin

运行ant任务，这里用于拷贝生成的jar到指定目录。  
antrun学习地址：https://www.javatpoint.com/apache-ant-tutorial

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-antrun-plugin</artifactId>
    <version>${build.plugin.antrun.version}</version>
    <executions>
        <execution>
            <phase>package</phase>
            <goals>
                <goal>run</goal>
            </goals>
            <configuration>
                <tasks>
                    <echo message="compile classpath: ${lib.dir}"/>
                    <!-- ${project.build.directory}这个是tartget目录 -->
                    <!-- 这句话的意思是将${artifactId}-${version}.zip包里面的内容复制到 -->
                    <copy overwrite="true"
                          todir="${lib.dir}"
                          file="${project.build.directory}/${project.artifactId}.jar"/>
                    <delete>
                        <fileset dir="${lib.dir}" includes="*.pom"/>
                    </delete>
                </tasks>
            </configuration>
        </execution>
    </executions>
</plugin>
```