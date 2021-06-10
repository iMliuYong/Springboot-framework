package com.quickshare.common.bean.web;

import java.util.List;

/**
 * ArrayData
 */
public class ArrayData<T> {

    private List<T> data;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}