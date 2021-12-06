package com.czc.artjsj.log;

import com.czc.artjsj.ArtJsjCommNames;
import com.czc.artjsj.json.KJsonSingleton;
import com.czc.artjsj.utils.ExceptionUtils;
import com.czc.artjsj.utils.TextUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author CZC 控制台日志输出工具
 */
public class L {

    public static void e(HttpServletRequest request) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM-dd HH:mm:ss");
        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String, Object> headersMap = new HashMap<>();
        while (headerNames.hasMoreElements()) {
            String headerKey = headerNames.nextElement();
            String headerValue = request.getHeader(headerKey);
            headersMap.put(headerKey, headerValue);
        }

        Map<String, Object> requestOtherMap = new HashMap<>();
        requestOtherMap.put("requestTime", sdf.format(new Date()));
        requestOtherMap.put("method", request.getMethod());
        requestOtherMap.put("protocol", request.getProtocol());
        requestOtherMap.put("contextPath", request.getContextPath());
        requestOtherMap.put("servletPath", request.getServletPath());
        requestOtherMap.put("requestURI", request.getRequestURI());
        requestOtherMap.put("requestURL", request.getRequestURL().toString());

        Map<String, Object> requestContent = new HashMap<>();
        requestContent.put("headers", headersMap);
        requestContent.put("params", request.getParameterMap());
        requestContent.put("otherInfo", requestOtherMap);
        baseE(KJsonSingleton.getJson(requestContent), 5, null);
    }

    public static void e(Object text) {
        baseE(text, 5, null);
    }

    public static void e(Object text, Exception e) {
        baseE(text, 5, e);
    }

    public static void e(Object text, int num) {
        baseE(text, num, null);
    }

    public static void e(Object text, int num, Exception e) {
        baseE(text, num, e);
    }

    private static void baseE(Object text, int num, Exception e) {
        if (text == null)
            return;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM-dd HH:mm:ss");
        if (ArtJsjCommNames.debug) {
            if (TextUtils.isEmpty(text.toString())) {
                System.err.println("[" + ArtJsjCommNames.TAG + "]" + "[" + sdf.format(new Date()) + "]" + "["
                        + (e == null ? ExceptionUtils.getRunInfo(num) : ExceptionUtils.getRunInfo(num, e)) + "] " + " - IS NULL");
            } else {
                System.err.println("[" + ArtJsjCommNames.TAG + "]" + "[" + sdf.format(new Date()) + "]" + "["
                        + (e == null ? ExceptionUtils.getRunInfo(num) : ExceptionUtils.getRunInfo(num, e)) + "] " + text);
            }
        }
    }
}
