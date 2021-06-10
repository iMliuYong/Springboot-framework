package com.quickshare.config.encryptor;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * @author liu_ke
 */
public interface Decryptor {

    public void decrypt(BufferedReader br) throws Exception;
}
