package com.quickshare.framework.web.client;

import com.quickshare.common.json.JsonConvert;
import com.quickshare.common.json.JsonUtils;
import com.quickshare.framework.http.MediaType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Rest请求客户端
 * @author liu_ke
 */
@Component
public class RestClient {

    private final RestTemplate restTemplate;

    public RestClient(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    public Object exchange(String url, HttpMethod httpMethod, Map<String,?> headers, Object body, Map<String,?> uriVariables){
        HttpHeaders header = createHeader(headers);
        HttpEntity<Object> httpEntity = new HttpEntity<>(body, header);
        ResponseEntity<String> response;
        if(uriVariables == null){
            response = restTemplate.exchange(url,httpMethod,httpEntity,String.class);
        }
        else{
            response = restTemplate.exchange(url,httpMethod,httpEntity,String.class,uriVariables);
        }
        String strResponse = response.getBody();
        if(JsonUtils.isJson(strResponse)){
            return JsonConvert.toObject(strResponse, Map.class);
        }
        else{
            return strResponse;
        }
    }

    /**
     * post请求
     * @param url 请求地址，包括格式化地址参数
     * @param headers 消息头参数
     * @param body 消息体
     * @param uriVariables 地址参数
     * @return
     */
    public Object post(String url,Map<String,?> headers,Object body,Map<String,?> uriVariables){
        return exchange(url, HttpMethod.POST,headers,body,uriVariables);
    }

    public Object get(String url,Map<String,?> headers,Map<String,?> uriVariables){
        return exchange(url,HttpMethod.GET,headers,null,uriVariables);
    }

    private HttpHeaders createHeader(Map<String,?> headers){
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON_UTF8);
        if(headers != null && headers.size() > 0){
            for (Map.Entry<String,?> item: headers.entrySet()) {
                if(item.getValue() != null){
                    header.add(item.getKey(), item.getValue().toString());
                }
                else{
                    header.add(item.getKey(), null);
                }
            }
        }
        return header;
    }
}
