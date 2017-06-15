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

package top.shixinzhang.sxframework.network.upload;

import android.text.TextUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import top.shixinzhang.sxframework.utils.LogUtils;

/**
 * Description:
 * <br> 上传文件，使用 okhttp3 实现
 * <p>
 * <br> Created by shixinzhang on 17/6/15.
 * <p>
 * <br> Email: shixinzhang2016@gmail.com
 * <p>
 * <br> https://about.me/shixinzhang
 */

public class UploadManager {

    private final String TAG = this.getClass().getSimpleName();

    private OkHttpClient mHttpClient;
    private String mName;
    private String mFileName;
    private String mFileType;
    private File mFile;
    private String mUrl;
    private Map<String, String> mHeaders;
    private Callback mCallback;

    private OkHttpClient newClient() {
        return new OkHttpClient.Builder()
//                .addInterceptor()
                .build();
    }

    private UploadManager(final Builder builder) {
        mHttpClient = newClient();
        mName = builder.mName;
        mFileName = builder.mFileName;
        mFileType = builder.mFileType;
        mFile = builder.mFile;
        mUrl = builder.mUrl;
        mHeaders = builder.mHeaders;
        mCallback = builder.mCallback;

        checkArguments();
    }

    private void checkArguments() {
        if (TextUtils.isEmpty(mName) || TextUtils.isEmpty(mFileType) || mFile == null || TextUtils.isEmpty(mUrl)) {
            throw new IllegalArgumentException("以下上传所需参数不能为空：上传 key、上传文件类型、上传文件、上传地址");
        }
    }

    public void upload() {
        MultipartBody multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(mName, mFileName, RequestBody.create(MediaType.parse(mFileType), mFile))
                .build();

        Request.Builder builder = new Request.Builder()
                .url(mUrl)
                .post(multipartBody);

        if (mHeaders != null) {
            builder.headers(Headers.of(mHeaders));
        }

        mHttpClient.newCall(builder.build()).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                e.printStackTrace();
                LogUtils.d(TAG, "上传失败：" + e.getMessage());

                if (mCallback != null) {
                    mCallback.onFailure(call, e);
                }
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                LogUtils.d(TAG, "上传成功：" + response.body().string());

                if (mCallback != null) {
                    mCallback.onResponse(call, response);
                }
            }
        });


    }

    public static final class Builder {
        private String mName;
        private String mFileName;
        private String mFileType;
        private File mFile;
        private String mUrl;
        private Map<String, String> mHeaders;
        private Callback mCallback;

        public Builder() {
        }

        public Builder name(final String mName) {
            this.mName = mName;
            return this;
        }

        public Builder fileName(final String mFileName) {
            this.mFileName = mFileName;
            return this;
        }

        public Builder fileType(final String mFileType) {
            this.mFileType = mFileType;
            return this;
        }

        public Builder file(final File mFile) {
            this.mFile = mFile;
            return this;
        }

        public Builder url(final String mUrl) {
            this.mUrl = mUrl;
            return this;
        }

        public Builder headers(final Map<String, String> mHeaders) {
            this.mHeaders = mHeaders;
            return this;
        }

        public Builder callback(final Callback mCallback) {
            this.mCallback = mCallback;
            return this;
        }

        public UploadManager build() {
            return new UploadManager(this);
        }
    }
}
