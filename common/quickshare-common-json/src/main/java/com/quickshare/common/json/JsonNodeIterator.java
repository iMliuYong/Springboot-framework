package com.quickshare.common.json;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Iterator;

/**
 * @author liu_ke
 */
public class JsonNodeIterator {

    private final Iterator<JsonNode> _iterator;

    public JsonNodeIterator(Iterator<JsonNode> iterator){
        this._iterator = iterator;
    }

    public boolean hasNext(){
        return _iterator.hasNext();
    }

    public com.quickshare.common.json.JsonNode next(){
        return new com.quickshare.common.json.JsonNode(_iterator.next());
    }
}
