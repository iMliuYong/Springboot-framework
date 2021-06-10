package com.quickshare.samples.environment;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author liu_ke
 */
@Mapper
public interface TestMapper {

    @Select("<script>select Code,Name from Com_Hospital</script>")
    List<Map<String,Object>> lstHospitals();
}
