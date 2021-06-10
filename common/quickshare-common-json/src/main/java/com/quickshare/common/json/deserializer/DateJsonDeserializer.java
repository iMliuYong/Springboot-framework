package com.quickshare.common.json.deserializer;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.apache.commons.lang3.time.DateUtils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author liu_ke
 */
public class DateJsonDeserializer extends JsonDeserializer<Date> {

    private final static String FORMAT_19 = "yyyy-MM-dd HH:mm:ss";

    private final static String FORMAT_10 = "yyyy-MM-dd";

    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        try {
            Date date = null;
            String dateValue = jsonParser.getText();
            if(dateValue.length() == 10){
                date = DateUtils.parseDate(dateValue,FORMAT_10);
            }
            else if(dateValue.length() == 19){
                date = DateUtils.parseDate(dateValue,FORMAT_19);
            }
            return date;
        } catch (ParseException | SecurityException e) {
            return null;
        }
    }
}
