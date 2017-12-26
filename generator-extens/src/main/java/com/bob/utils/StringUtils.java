package com.bob.utils;

/**
 * Created by wangxiang on 17/8/11.
 */
public final class StringUtils {

    public static boolean isNullOrEmpty(final String input) {
        return input == null || input.trim().length() == 0;
    }
}