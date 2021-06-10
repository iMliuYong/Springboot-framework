package com.quickshare.config.encryptor;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * @author liu_ke
 */
public interface Encryptor {

    /**
     * 加密处理
     */
    void encrypt(BufferedReader br) throws Exception;
}
