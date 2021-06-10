package com.quickshare.samples.datasource.mapper;

import com.quickshare.samples.datasource.bean.TestTable;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author liu_ke
 */
@Mapper
public interface TestMapper {

    @Insert("<script>insert into testTable ( col1 ) values(#{col1})</script>")
    int add(@Param("col1") String col1);

    List<TestTable> query();
}
