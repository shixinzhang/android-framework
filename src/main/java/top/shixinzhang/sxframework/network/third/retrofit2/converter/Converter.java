/*
 * Copyright (C) 2012 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package top.shixinzhang.sxframework.network.third.retrofit2.converter;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import top.shixinzhang.sxframework.network.third.retrofit2.http.*;
import top.shixinzhang.sxframework.network.third.retrofit2.request.Retrofit;

/**
 * 从 F 到 T 的转换
 * <p>
 * Convert objects to and from their representation in HTTP. Instances are created by {@linkplain
 * Factory a factory} which is Retrofit.Builder#addConverterFactory(Factory) installed
 * into the {@link Retrofit} instance.
 */
public interface Converter<F, T> {
    T convert(F value) throws IOException;

    /**
     * 提供转换器的工厂
     * <p>
     * Creates {@link Converter} instances based on a type and target usage.
     */
    abstract class Factory {
        /**
         * Returns a {@link Converter} for converting an HTTP response body to {@code type}, or null if
         * {@code type} cannot be handled by this factory. This is used to create converters for
         * response types such as {@code SimpleResponse} from a {@code Call<SimpleResponse>}
         * declaration.
         */
        public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                                Retrofit retrofit) {
            return null;
        }

        /**
         * Returns a {@link Converter} for converting {@code type} to an HTTP request body, or null if
         * {@code type} cannot be handled by this factory. This is used to create converters for types
         * specified by {@link Body @Body}, {@link Part @Part}, and {@link PartMap @PartMap}
         * values.
         */
        public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                              Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
            return null;
        }

        /**
         * 这里用于对Field、FieldMap、Header、Path、Query、QueryMap注解的处理
         * Retrofit 对于上面的几个注解默认使用的是调用toString方法
         * <p>
         * Returns a {@link Converter} for converting {@code type} to a {@link String}, or null if
         * {@code type} cannot be handled by this factory. This is used to create converters for types
         * specified by {@link Field @Field}, {@link FieldMap @FieldMap} values,
         * {@link Header @Header}, {@link HeaderMap @HeaderMap}, {@link Path @Path},
         * {@link Query @Query}, and {@link QueryMap @QueryMap} values.
         */
        public Converter<?, String> stringConverter(Type type, Annotation[] annotations,
                                                    Retrofit retrofit) {
            return null;
        }
    }
}