package com.czc.artjsj.utils;

import com.czc.artjsj.ArtJsjCommNames;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * @author CZC Exception工具
 */
public class ExceptionUtils {

    /**
     * Exception信息解析获取，可以获取到具体哪一行报错
     */
    private static String getExceptionAllinformation(Exception e) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream pout = new PrintStream(out);
        e.printStackTrace(pout);
        String ret = out.toString();
        pout.close();
        try {
            out.close();
        } catch (Exception e2) {
            return ArtJsjCommNames.TAG + " - ExceptionUtils - " + e2.getMessage();
        }
        return ret;
    }

    public static String getRunInfo() {
        try {
            return baseRunInfo(0);
        } catch (Exception e) {
            return ArtJsjCommNames.TAG + " - ExceptionUtils - " + e.getMessage();
        }
    }

    public static String getRunInfo(int stackTraceNum) {
        try {
            return baseRunInfo(stackTraceNum);
        } catch (Exception e) {
            return ArtJsjCommNames.TAG + " - ExceptionUtils - " + e.getMessage();
        }
    }

    public static String getRunInfo(Exception e) {
        try {
            return baseRunInfo(0) + System.getProperty("line.separator") + "Exception："
                    + getExceptionAllinformation(e);
        } catch (Exception e1) {
            return ArtJsjCommNames.TAG + " - ExceptionUtils - " + e1.getMessage();
        }
    }

    public static String getRunInfo(int stackTraceNum, Exception e) {
        try {
            return baseRunInfo(stackTraceNum) + System.getProperty("line.separator") + "Exception："
                    + getExceptionAllinformation(e);
        } catch (Exception e1) {
            return ArtJsjCommNames.TAG + " - ExceptionUtils - " + e1.getMessage();
        }
    }


    private static String baseRunInfo(int stackTraceNum) throws Exception {
        // Thread.currentThread().getStackTrace()[1]是你当前方法执行堆栈
        // Thread.currentThread().getStackTrace()[2]就是上一级的方法堆栈 以此类推
        // StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        // for (StackTraceElement stackTraceElement : stackTraceElements) {
        //     System.out.println(stackTraceElement.getMethodName() + "-" + stackTraceElement.getMethodName() + "-"
        //             + stackTraceElement.getLineNumber());
        // }
        String className = Thread.currentThread().getStackTrace()[stackTraceNum].getClassName();// 类名
        return className.substring(className.lastIndexOf(".") + 1) + "-"
                + Thread.currentThread().getStackTrace()[stackTraceNum].getMethodName() + "-"
                + Thread.currentThread().getStackTrace()[stackTraceNum].getLineNumber();
    }


}