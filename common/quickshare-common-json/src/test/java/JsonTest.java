import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@DisplayName("Json测试类")
public class JsonTest {

    private static final ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES,true);
    }

    @Test
    @DisplayName("读取json测试")
    public void readTest() throws JsonProcessingException {
        String json = FileUtil.readFile("readTest.json");
        JsonNode jsonNode = mapper.readTree(json);
        String fieldName = "aa";
        Iterator<String> fieldNames = jsonNode.fieldNames();
        Map<String,String> fieldNameMap = new HashMap<>();
        while (fieldNames.hasNext()){
            String name = fieldNames.next();
            fieldNameMap.put(name.toLowerCase(),name);
        }
        String realFieldName = fieldNameMap.get(fieldName.toLowerCase());
        JsonNode value = jsonNode.get(realFieldName);
        String realValue = value.asText();
        System.out.println(realValue);
    }
}
