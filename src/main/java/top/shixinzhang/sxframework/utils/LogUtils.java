/*
 * Copyright (c) 2017. shixinzhang (shixinzhang2016@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package top.shixinzhang.sxframework.utils;

import android.text.TextUtils;
import android.util.Log;

import java.io.File;

import top.shixinzhang.sxframework.AppInfo;

/**
 * <br> Description: 日志输出类
 * <p>
 * <br> Created by shixinzhang on 17/4/21.
 * <p>
 * <br> Email: shixinzhang2016@gmail.com
 * <p>
 * <a  href="https://about.me/shixinzhang">About me</a>
 */

public final class LogUtils {
    private static final String TAG = "LogUtils";

    /**
     * Priority constant for the println method
     */
    private static final int VERBOSE = 2;
    private static final int DEBUG = 3;
    private static final int INFO = 4;
    private static final int WARN = 5;
    private static final int ERROR = 6;

    /**
     * 是否显示日志，默认为 true
     */
    private volatile static boolean isDebug = true;

    private volatile static boolean shouldWriteFile = true;

    private static boolean isShouldWriteFile() {
        return shouldWriteFile;
    }

    private static void setShouldWriteFile(boolean shouldWriteFile) {
        LogUtils.shouldWriteFile = shouldWriteFile;
    }

    private static boolean isDebug() {
        return isDebug;
    }

    private static void setDebug(boolean isDebug) {
        LogUtils.isDebug = isDebug;
    }

    private LogUtils() {
    }

    public static void v(String log) {
        v(TAG, log);
    }

    public static void d(String log) {
        d(TAG, log);
    }

    public static void i(String log) {
        i(TAG, log);
    }

    public static void w(String log) {
        w(TAG, log);
    }

    public static void e(String log) {
        e(TAG, log);
    }

    public static void v(String tag, String log) {
        println(tag, log, VERBOSE);
    }

    public static void d(String tag, String log) {
        println(tag, log, DEBUG);
    }

    public static void i(String tag, String log) {
        println(tag, log, INFO);
    }

    public static void w(String tag, String log) {
        println(tag, log, WARN);
    }

    public static void e(String tag, String log) {
        println(tag, log, ERROR);
    }

    public static void println(String tag, String log, int type) {
        if (TextUtils.isEmpty(tag) || TextUtils.isEmpty(log)) {
            return;
        }

        if (!isDebug){
            // TODO: 17/4/21 当不输出日志时，可以考虑将日志文件保存，上传
            return;
        }

        if (shouldWriteFile){
            if (!log.endsWith("\n"))
                log = log.concat("\n");

            FileUtils.writeFile(getLogFilePath(), DateFormatUtils.getDateString(System.currentTimeMillis()) + " " + log, true);
        }

        switch (type) {
            default:
            case VERBOSE:
                Log.v(tag, log);
                break;
            case DEBUG:
                Log.d(tag, log);
                break;
            case INFO:
                Log.i(tag, log);
                break;
            case WARN:
                Log.w(tag, log);
                break;
            case ERROR:
                Log.e(tag, log);
                break;
        }
    }

    private static String getLogFilePath() {
        return AppInfo.DIRECTORY_PATH + File.separator + "log.txt";
    }
}
