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

package top.shixinzhang.sxframework.inject.third.butterknife.annotation;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;


import top.shixinzhang.sxframework.inject.third.butterknife.annotation.internal.ListenerClass;
import top.shixinzhang.sxframework.inject.third.butterknife.annotation.internal.ListenerMethod;
import static android.view.View.OnClickListener;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Bind a method to an {@link OnClickListener OnClickListener} on the view for each ID specified.
 * <pre><code>
 * {@literal @}OnClick(R.id.example) void onClick() {
 *   Toast.makeText(this, "Clicked!", Toast.LENGTH_SHORT).show();
 * }
 * </code></pre>
 * Any number of parameters from
 * {@link OnClickListener#onClick(View) onClick} may be used on the
 * method.
 *
 * @see OnClickListener
 */
@Target(METHOD)
@Retention(CLASS)
@ListenerClass(
    targetType = "android.view.View",
    setter = "setOnClickListener",
    type = "butterknife.internal.DebouncingOnClickListener",
    method = @ListenerMethod(
        name = "doClick",
        parameters = "android.view.View"
    )
)
public @interface OnClick {
  /** View IDs to which the method will be bound. */
  @NonNull @IdRes int[] value() default { View.NO_ID };
}
