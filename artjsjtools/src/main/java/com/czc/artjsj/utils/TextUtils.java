package com.czc.artjsj.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtils {
    public static boolean isEmpty(String str) {
        return !hasText(str);
    }

    public static boolean hasText(String str) {
        return str != null && !str.isEmpty() && containsText(str);
    }

    private static boolean containsText(CharSequence str) {
        int strLen = str.length();
        for (int i = 0; i < strLen; ++i) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 取出一个字符串中的所有汉字
     *
     * @param content 需要处理的内容
     * @return String 取出后的结果
     */
    public static String getChinese(String content) {
        String regex = "([\u4e00-\u9fa5]+)";// 中文正则
        String str = "";
        Matcher matcher = Pattern.compile(regex).matcher(content);
        while (matcher.find()) {
            str += matcher.group(0);
        }
        return str;
    }

    /**
     * 删除一个字符串中的所有汉字
     *
     * @param content 需要处理的内容
     * @return String 删除后的结果
     */
    public static String delChinese(String content) {
        String REGEX_CHINESE = "[\u4e00-\u9fa5]";// 中文正则
        // 去除中文
        Pattern pat = Pattern.compile(REGEX_CHINESE);
        Matcher mat = pat.matcher(content);
        return mat.replaceAll("");
    }
}
