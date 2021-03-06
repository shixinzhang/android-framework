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

package top.shixinzhang.sxframework.eventsubscribe.third.rxbus.thread;

import android.os.Looper;
import android.support.annotation.Nullable;

import top.shixinzhang.sxframework.eventsubscribe.third.rxbus.Bus;


/**
 * Enforces a thread confinement policy for methods on a particular event bus.
 */
public interface ThreadEnforcer {

    /**
     * Enforce a valid thread for the given {@code bus}. Implementations may throw any runtime exception.
     *
     * @param bus Event bus instance on which an action is being performed.
     */
    void enforce(Bus bus);


    /**
     * A {@link ThreadEnforcer} that does no verification.
     */
    ThreadEnforcer ANY = new ThreadEnforcer() {
        @Override
        public void enforce(Bus bus) {
            // Allow any thread.
        }
    };

    /**
     * A {@link ThreadEnforcer} that confines {@link Bus} methods to the main thread.
     */
    @Nullable
    ThreadEnforcer MAIN = new ThreadEnforcer() {
        @Override
        public void enforce(Bus bus) {
            if (Looper.myLooper() != Looper.getMainLooper()) {
                throw new IllegalStateException("Event bus " + bus + " accessed from non-main thread " + Looper.myLooper());
            }
        }
    };

}
