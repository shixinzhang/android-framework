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
package top.shixinzhang.sxframework.imageload.picasso;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

class FetchAction extends Action<Object> {

  @NonNull
  private final Object target;
  @Nullable
  private Callback callback;

  FetchAction(@NonNull Picasso picasso, Request data, int memoryPolicy, int networkPolicy, Object tag,
              String key, Callback callback) {
    super(picasso, null, data, memoryPolicy, networkPolicy, 0, null, key, tag, false);
    this.target = new Object();
    this.callback = callback;
  }

  @Override void complete(Bitmap result, Picasso.LoadedFrom from) {
    if (callback != null) {
      callback.onSuccess();
    }
  }

  @Override void error() {
    if (callback != null) {
      callback.onError();
    }
  }

  @Override void cancel() {
    super.cancel();
    callback = null;
  }

  @NonNull
  @Override Object getTarget() {
    return target;
  }
}
