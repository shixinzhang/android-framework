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

package top.shixinzhang.sxframework.eventsubscribe.third.eventbus.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;


import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import top.shixinzhang.sxframework.eventsubscribe.third.eventbus.EventBus;


/**
 * Maps throwables to texts for error dialogs. Use Config to configure the mapping.
 * 
 * @author Markus
 */
public class ExceptionToResourceMapping {

    @NonNull
    public final Map<Class<? extends Throwable>, Integer> throwableToMsgIdMap;

    public ExceptionToResourceMapping() {
        throwableToMsgIdMap = new HashMap<Class<? extends Throwable>, Integer>();
    }

    /** Looks at the exception and its causes trying to find an ID. */
    @Nullable
    public Integer mapThrowable(final Throwable throwable) {
        Throwable throwableToCheck = throwable;
        int depthToGo = 20;

        while (true) {
            Integer resId = mapThrowableFlat(throwableToCheck);
            if (resId != null) {
                return resId;
            } else {
                throwableToCheck = throwableToCheck.getCause();
                depthToGo--;
                if (depthToGo <= 0 || throwableToCheck == throwable || throwableToCheck == null) {
                    Log.d(EventBus.TAG, "No specific message ressource ID found for " + throwable);
                    // return config.defaultErrorMsgId;
                    return null;
                }
            }
        }

    }

    /** Mapping without checking the cause (done in mapThrowable). */
    protected Integer mapThrowableFlat(@NonNull Throwable throwable) {
        Class<? extends Throwable> throwableClass = throwable.getClass();
        Integer resId = throwableToMsgIdMap.get(throwableClass);
        if (resId == null) {
            Class<? extends Throwable> closestClass = null;
            Set<Entry<Class<? extends Throwable>, Integer>> mappings = throwableToMsgIdMap.entrySet();
            for (Entry<Class<? extends Throwable>, Integer> mapping : mappings) {
                Class<? extends Throwable> candidate = mapping.getKey();
                if (candidate.isAssignableFrom(throwableClass)) {
                    if (closestClass == null || closestClass.isAssignableFrom(candidate)) {
                        closestClass = candidate;
                        resId = mapping.getValue();
                    }
                }
            }

        }
        return resId;
    }

    @NonNull
    public ExceptionToResourceMapping addMapping(Class<? extends Throwable> clazz, int msgId) {
        throwableToMsgIdMap.put(clazz, msgId);
        return this;
    }

}
