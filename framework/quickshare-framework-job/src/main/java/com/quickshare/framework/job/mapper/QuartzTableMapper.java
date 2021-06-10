package com.quickshare.framework.job.mapper;

import org.apache.ibatis.annotations.Mapper;

/**
 * @author liu_ke
 */
@Mapper
public interface QuartzTableMapper {

    void initTable();
}