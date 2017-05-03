package top.shixinzhang.sxframework.network.download.imp;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import top.shixinzhang.sxframework.utils.ApplicationUtil;
import top.shixinzhang.sxframework.utils.LogUtil;
import top.shixinzhang.sxframework.utils.SpUtil;

/**
 * Description:
 * <br> android 自带的 DownloadManager 下载完成后会发出一个广播 android.intent.action.DOWNLOAD_COMPLETE
 * <p>
 * <br> Created by shixinzhang on 17/4/27.
 * <p>
 * <br> Email: shixinzhang2016@gmail.com
 * <p>
 * <a  href="https://about.me/shixinzhang">About me</a>
 */

public class DefaultDownloadReceiver extends BroadcastReceiver {
    private final String TAG = this.getClass().getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())) {
            long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            LogUtil.d(TAG, downloadId + " download success");

            //弹出提示用户安装
            if (downloadId != -1) {
                installApk(context, downloadId);
            }
        }
    }

    private void installApk(Context context, long downloadSuccessId) {

        long lastDownloadId = SpUtil.getDataFromDefault(context, DownloadManager.EXTRA_DOWNLOAD_ID, -1L);
        if (downloadSuccessId == lastDownloadId) {
            DownloadManager downManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            Uri downloadFileUri = downManager.getUriForDownloadedFile(downloadSuccessId);
            if (downloadFileUri != null) {
                ApplicationUtil.installPackage(context, downloadFileUri);
            } else {
                Toast.makeText(context, "下载失败", Toast.LENGTH_SHORT).show();
            }
        }
    }
}