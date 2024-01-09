package com.alinesno.infra.base.search.gateway.utils;

import java.security.SecureRandom;

/**
 * 集成工具类
 */
public class CollectionUtils {

    public static String generateUniqueString() {
        int length = 8;
        String characters = "abcdefghijklmnopqrstuvwxyz";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            sb.append(characters.charAt(randomIndex));
        }

        return sb.toString();
    }

}
