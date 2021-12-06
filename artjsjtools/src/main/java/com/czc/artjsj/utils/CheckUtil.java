package com.czc.artjsj.utils;

/**
 * 检验工具
 *
 * @author CZC
 */
public class CheckUtil {
    /**
     * 验证是否是一个正确的UR
     *
     * @param url 需要判断的url
     * @return boolean true:合法 false:不合法
     * @author CZC
     */
    public static boolean isUrl(String url) {
        return url.matches("^((https|http|ftp|rtsp|mms)?:\\/\\/)[^\\s]+");
    }

    /**
     * 验证是否是合法的中国身份证号
     *
     * @param num 需要判断的身份证号码
     * @return boolean true:合法 false:不合法
     * @author CZC
     */
    public static boolean isChinaIdentityCard(String num) {
        return num.matches("\\d{17}[\\d|x]|\\d{15}");
    }

    /**
     * 验证是否是合法的中国手机号
     *
     * @param num 需要判断的身份证号码
     * @return boolean true:合法 false:不合法
     * @author CZC
     */
    public static boolean isChinaPho(String num) {
        return num.matches("0?(13|14|15|18|17)[0-9]{9}");
    }

    /**
     * 验证是否是合法邮箱地址
     *
     * @param email 需要判断的邮箱地址
     * @return boolean true:合法 false:不合法
     * @author CZC
     */
    public static boolean isEmail(String email) {
        return email.matches("\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}");
    }

    /**
     * 验证是否是合法IP地址
     *
     * @param ip 需要判断的IP
     * @return boolean true:合法 false:不合法
     * @author CZC
     */
    public static boolean isIp(String ip) {
        return ip.matches(
                "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)");
    }

    /**
     * 判断一个字符串是否含有数字
     *
     * @param content 需要判断的内容
     * @return boolean true:有 false:无
     * @author CZC
     */
    public static boolean haveDigit(String content) {
        return content.matches(".*\\d+.*");
    }
}
