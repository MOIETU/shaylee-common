package com.shaylee.core.utils;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Title: Base64加密解密工具
 * Project: shaylee-common
 *
 * @author Adrian
 * @version 1.0
 * @date 2020-2-26
 */
public class Base64Utils {

    /**
     * 加密BASE64
     *
     * @param inputStream 输入流
     * @return 加密后的Base64
     */
    public static String encryptBase64(InputStream inputStream) throws Exception {
        return encryptBase64(IOUtils.toByteArray(inputStream));
    }

    /**
     * 加密BASE64
     *
     * @param data 数据
     * @return 加密后的Base64
     */
    public static String encryptBase64(byte[] data) {
        return new String(Base64.getEncoder().encode(data), StandardCharsets.ISO_8859_1);
    }

    /**
     * 解密BASE64
     *
     * @param data Base64数据字符串
     * @return 解密后的数据
     */
    public static byte[] decryptBase64(String data) {
        return Base64.getDecoder().decode(data);
    }

    /**
     * 解密BASE64
     *
     * @param data Base64数据字符串
     * @return 解密后的数据
     */
    public static InputStream decryptBase64Stream(String data) {
        return new ByteArrayInputStream(Base64.getDecoder().decode(data));
    }
}
