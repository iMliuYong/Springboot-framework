package com.quickshare.common.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Json节点
 * @author liu_ke
 */
public class JsonNode {

    private static final ObjectMapper mapper = ObjectMapperFactory.getMapper();

    private final Map<String, com.fasterxml.jackson.databind.JsonNode> _lowerCaseKeyChildren=new HashMap<>();
    private final Map<String, JsonNode> _children = new HashMap<>();
    private final com.fasterxml.jackson.databind.JsonNode _parent;

    public JsonNode(com.fasterxml.jackson.databind.JsonNode parent){
        this._parent = parent;
    }

    public JsonNode(Object o) {
        try {
            if(o instanceof com.fasterxml.jackson.databind.JsonNode){
                this._parent = (com.fasterxml.jackson.databind.JsonNode)o;
            }
            else if(o instanceof String){
                this._parent = mapper.readTree((String) o);
            }
            else if(o instanceof InputStream){
                this._parent = mapper.readTree((InputStream) o);
            }
            else if(o instanceof Reader){
                this._parent = mapper.readTree((Reader) o);
            }
            else if(o instanceof byte[]){
                this._parent = mapper.readTree((byte[]) o);
            }
            else if(o instanceof File){
                this._parent = mapper.readTree((File) o);
            }
            else {
                this._parent = mapper.valueToTree(o);
            }
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public JsonNode getNode(String fieldName){
        JsonNode node = _children.get(fieldName);
        if(node == null){
            com.fasterxml.jackson.databind.JsonNode jsonNode = getData(fieldName);
            if(jsonNode != null){
                node = new JsonNode(jsonNode);
                _children.put(fieldName,node);
            }
        }
        return node;
    }

    public JsonNode deepCopy(){
        com.fasterxml.jackson.databind.JsonNode jsonNode = _parent.deepCopy();
        return new JsonNode(jsonNode);
    }

    public String getValue(String fieldName){
        com.fasterxml.jackson.databind.JsonNode jsonNode = getData(fieldName);
        if(jsonNode == null){
            return null;
        }
        return jsonNode.asText();
    }

    public void setValue(String fieldName,Object value){
        if(!(_parent instanceof ObjectNode)){
            return;
        }
        ObjectNode objectNode = (ObjectNode)_parent;
        if(value instanceof Short){
            objectNode.put(fieldName,(Short)value);
        }
        else if(value instanceof Integer){
            objectNode.put(fieldName,(Integer)value);
        }
        else if(value instanceof Long){
            objectNode.put(fieldName,(Long)value);
        }
        else if(value instanceof Float){
            objectNode.put(fieldName,(Float)value);
        }
        else if(value instanceof Double){
            objectNode.put(fieldName,(Double)value);
        }
        else if(value instanceof BigDecimal){
            objectNode.put(fieldName,(BigDecimal)value);
        }
        else if(value instanceof BigInteger){
            objectNode.put(fieldName,(BigInteger)value);
        }
        else if(value instanceof String){
            objectNode.put(fieldName,(String)value);
        }
        else if(value instanceof Boolean){
            objectNode.put(fieldName,(Boolean)value);
        }
        else if(value instanceof byte[]){
            objectNode.put(fieldName,(byte[])value);
        }
    }

    public Object getData(){
        return _parent;
    }

    public boolean isObject(){
        return _parent.isObject();
    }

    public boolean isArray(){
        return _parent.isArray();
    }

    public JsonNodeIterator iterator(){
        return new JsonNodeIterator(_parent.iterator());
    }

    private com.fasterxml.jackson.databind.JsonNode getData(String fieldName) {
        com.fasterxml.jackson.databind.JsonNode jsonNode = _parent.get(fieldName);
        if(jsonNode == null){
            jsonNode = getLowerCaseKeyChildren().get(fieldName.toLowerCase());
        }
        return jsonNode;
    }

    private Map<String, com.fasterxml.jackson.databind.JsonNode> getLowerCaseKeyChildren(){
        if(!_lowerCaseKeyChildren.isEmpty()){
            return _lowerCaseKeyChildren;
        }
        Iterator<Map.Entry<String, com.fasterxml.jackson.databind.JsonNode>> iterator = _parent.fields();
        while (iterator.hasNext()){
            Map.Entry<String, com.fasterxml.jackson.databind.JsonNode> jsonNodeEntry = iterator.next();
            _lowerCaseKeyChildren.put(jsonNodeEntry.getKey().toLowerCase(),jsonNodeEntry.getValue());
        }
        return _lowerCaseKeyChildren;
    }
}
