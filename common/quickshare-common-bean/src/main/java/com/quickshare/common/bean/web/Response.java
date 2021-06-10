package com.quickshare.common.bean.web;

public class Response<T> extends BaseResponse<T> {
  
    private long total;

    public Response(){
      super();
    }

    public Response(int code,String message,T data) {
      super(code, message, data);
    }
    
    public Response(int code,String message,T data,long total) {
      super(code, message, data);
      this.total = total;
    }

    public Response(T data,long total) {
      super(0, null, data);
      this.total = total;
    }

    public Response(T data) {
      super(0, null, data);
    }
    
    public long getTotal() {
      return total;
    }
    
    public void setTotal(long total) {
    }
}