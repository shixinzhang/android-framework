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

package top.shixinzhang.sxframework.statistic;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

import top.shixinzhang.sxframework.AppInfo;
import top.shixinzhang.sxframework.BuildConfig;
import top.shixinzhang.utils.AlertUtils;
import top.shixinzhang.utils.ApplicationUtils;
import top.shixinzhang.utils.DateUtils;
import top.shixinzhang.utils.FileUtils;

/**
 * <br> Description:
 * <p> 错误捕获
 * http://www.jianshu.com/p/3b66f15babfb
 * http://qkxue.net/info/185791/Android
 * <p>
 * <br> Created by shixinzhang on 17/4/24.
 * <p>
 * <br> Email: shixinzhang2016@gmail.com
 * <p>
 * <a  href="https://about.me/shixinzhang">About me</a>
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private Context mContext;
    public boolean openUpload = true;
    private static final String DEFAULT_LOG_DIR = "log";
    private static final String FILE_NAME_SUFFIX = ".log";
    @Nullable
    private static volatile CrashHandler sInstance = null;
    private Thread.UncaughtExceptionHandler mDefaultCrashHandler;
    @NonNull
    private static volatile List<OnCrashCallback> mCrashCallbackList = new LinkedList<>();

    /**
     * 奔溃回调观察者
     */
    public interface OnCrashCallback {
        void onCrash(Throwable throwable);
    }

    public static void registerCallback(@Nullable OnCrashCallback callback) {
        if (callback != null) {
            mCrashCallbackList.add(callback);
        }
    }

    public static void unRegisterCallback(@Nullable OnCrashCallback callback) {
        if (callback != null) {
            mCrashCallbackList.remove(callback);
        }
    }

    private CrashHandler(@NonNull Context cxt) {
        this.mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();

        Thread.setDefaultUncaughtExceptionHandler(this);

        this.mContext = cxt.getApplicationContext();
    }

    @Nullable
    public static synchronized CrashHandler init(@NonNull Context cxt) {
        if (sInstance == null) {
            sInstance = new CrashHandler(cxt);
        }
        return sInstance;
    }


    @Override
    public void uncaughtException(Thread thread, @NonNull Throwable ex) {

        ex.printStackTrace();

        notifyObservers(ex);

        try {
            saveToSDCard(ex);
//			ActivityPageManager.getInstance().finishAllActivity();
//			MobclickAgent.onKillProcess(YourApplication.getInstance());
            // 联网发送
//			MobclickAgent.reportError(YourApplication.getInstance(), str);//发给友盟统计

//            Intent intent = new Intent(YourApplication.getInstance(), SplashActivity.class);
//            PendingIntent restartIntent = PendingIntent.getActivity(HaiBaoApplication.getInstance(), 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
//            AlarmManager mgr = (AlarmManager) HaiBaoApplication.getInstance().getSystemService(Context.ALARM_SERVICE);
//            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000,
//                    restartIntent); // 1秒钟后重启应用
//            System.exit(0);
            AlertUtils.toastShort(mContext, "啊偶，奔溃了");
            mDefaultCrashHandler.uncaughtException(thread, ex);
        } catch (Exception localException) {

        }
    }


    /**
     * 发送奔溃通知
     *
     * @param ex
     */
    private void notifyObservers(Throwable ex) {
        for (OnCrashCallback onCrashCallback : mCrashCallbackList) {
            onCrashCallback.onCrash(ex);
        }
    }


    private void saveToSDCard(@NonNull Throwable ex) throws Exception {
        //#1
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        if (BuildConfig.DEBUG) { //跳转到 Crash 详情页面

        }
//        FLog.i(writer.toString(), CRASH_LOG_PATH);

        //#2

        String currentDate = DateUtils.getDateString(System.currentTimeMillis());
        File file = FileUtils.createFile(AppInfo.DIRECTORY_PATH + currentDate + ".log");
        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));

        pw.println(currentDate);

        dumpPhoneInfo(pw);

        pw.println();

        ex.printStackTrace(pw);
        pw.flush();
        pw.close();
    }

    private void dumpPhoneInfo(@NonNull PrintWriter pw)
            throws PackageManager.NameNotFoundException {
        PackageInfo pi = ApplicationUtils.getCurrentAppInfo(mContext);
        if (pi == null) {
            return;
        }
        pw.print("App Version: ");
        pw.print(pi.versionName);
        pw.print('_');
        pw.println(pi.versionCode);
        pw.println();

        pw.print("OS Version: ");
        pw.print(Build.VERSION.RELEASE);
        pw.print("_");
        pw.println(Build.VERSION.SDK_INT);
        pw.println();

        pw.print("Vendor: ");
        pw.println(Build.MANUFACTURER);
        pw.println();

        pw.print("Model: ");
        pw.println(Build.MODEL);
        pw.println();

        pw.print("CPU ABI: ");
        pw.println(Build.CPU_ABI);
        pw.println();
    }


}