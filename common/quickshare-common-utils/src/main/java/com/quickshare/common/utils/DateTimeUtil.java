package com.quickshare.common.utils;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;

/**
 * @author liu_ke
 */
public class DateTimeUtil {

    public static Date currentDate(){
        return new Date(System.currentTimeMillis());
    }

    public static String currentStringDate(String format){
        return DateFormatUtils.format(currentDate(),format);
    }
}
