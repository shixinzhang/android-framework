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
package top.shixinzhang.sxframework.network.third.retrofit2.request;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


/**
 * 将 Call 转换成其他类型
 * <p>
 * Adapts a {@link Call} into the type of {@code T}. Instances are created by {@linkplain Factory a
 * factory} which is {@linkplain Retrofit.Builder#addCallAdapterFactory(Factory) installed} into
 * the {@link Retrofit} instance.
 */
public interface CallAdapter<R, T> {
    /**
     * Returns the value type that this adapter uses when converting the HTTP response body to a Java
     * object. For example, the response type for {@code Call<Repo>} is {@code Repo}. This type
     * is used to prepare the {@code call} passed to {@code #adapt}.
     * <p>
     * Note: This is typically not the same type as the {@code returnType} provided to this call
     * adapter's factory.
     */
    Type responseType();

    /**
     * Returns an instance of {@code T} which delegates to {@code call}.
     * <p>
     * For example, given an instance for a hypothetical utility, {@code Async}, this instance would
     * return a new {@code Async<R>} which invoked {@code call} when run.
     * <pre><code>
     * &#64;Override
     * public &lt;R&gt; Async&lt;R&gt; adapt(final Call&lt;R&gt; call) {
     *   return Async.create(new Callable&lt;Response&lt;R&gt;&gt;() {
     *     &#64;Override
     *     public Response&lt;R&gt; call() throws Exception {
     *       return call.execute();
     *     }
     *   });
     * }
     * </code></pre>
     */
    T adapt(Call<R> call);

    /**
     * Creates {@link CallAdapter} instances based on the return type of {@linkplain
     * Retrofit#create(Class) the service interface} methods.
     */
    abstract class Factory {
        /**
         * RxJavaCallAdapterFactory 就是判断 returnType 是不是Observable 类型
         * <p>
         * Returns a call adapter for interface methods that return {@code returnType}, or null if it
         * cannot be handled by this factory.
         */
        @Nullable
        public abstract CallAdapter<?, ?> get(Type returnType, Annotation[] annotations,
                                              Retrofit retrofit);

        /**
         * 用于获取泛型的参数列表 如 Call<Requestbody> 中 Requestbody
         * <p>
         * Extract the upper bound of the generic parameter at {@code index} from {@code type}. For
         * example, index 1 of {@code Map<String, ? extends Runnable>} returns {@code Runnable}.
         */
        protected static Type getParameterUpperBound(int index, @NonNull ParameterizedType type) {
            return Utils.getParameterUpperBound(index, type);
        }

        /**
         * 获取参数的最外层类型
         * <p>
         * Extract the raw class type from {@code type}. For example, the type representing
         * {@code List<? extends Runnable>} returns {@code List.class}.
         */
        @Nullable
        protected static Class<?> getRawType(Type type) {
            return Utils.getRawType(type);
        }
    }
}
