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

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Factory to allow injecting a more complex exception mapping; typically you would subclass one of {@link Honeycomb} or
 * {@link Support}.
 */
public abstract class ErrorDialogFragmentFactory<T> {
    protected final ErrorDialogConfig config;

    protected ErrorDialogFragmentFactory(ErrorDialogConfig config) {
        this.config = config;
    }

    /**
     * Prepares the fragment's arguments and creates the fragment. May be overridden to provide custom error fragments.
     */
    @Nullable
    protected T prepareErrorFragment(@NonNull ThrowableFailureEvent event, boolean finishAfterDialog,
                                     @Nullable Bundle argumentsForErrorDialog) {
        if (event.isSuppressErrorUi()) {
            // Show nothing by default
            return null;
        }
        Bundle bundle;
        if (argumentsForErrorDialog != null) {
            bundle = (Bundle) argumentsForErrorDialog.clone();
        } else {
            bundle = new Bundle();
        }

        if (!bundle.containsKey(ErrorDialogManager.KEY_TITLE)) {
            String title = getTitleFor(event, bundle);
            bundle.putString(ErrorDialogManager.KEY_TITLE, title);
        }
        if (!bundle.containsKey(ErrorDialogManager.KEY_MESSAGE)) {
            String message = getMessageFor(event, bundle);
            bundle.putString(ErrorDialogManager.KEY_MESSAGE, message);
        }
        if (!bundle.containsKey(ErrorDialogManager.KEY_FINISH_AFTER_DIALOG)) {
            bundle.putBoolean(ErrorDialogManager.KEY_FINISH_AFTER_DIALOG, finishAfterDialog);
        }
        if (!bundle.containsKey(ErrorDialogManager.KEY_EVENT_TYPE_ON_CLOSE)
                && config.defaultEventTypeOnDialogClosed != null) {
            bundle.putSerializable(ErrorDialogManager.KEY_EVENT_TYPE_ON_CLOSE, config.defaultEventTypeOnDialogClosed);
        }
        if (!bundle.containsKey(ErrorDialogManager.KEY_ICON_ID) && config.defaultDialogIconId != 0) {
            bundle.putInt(ErrorDialogManager.KEY_ICON_ID, config.defaultDialogIconId);
        }
        return createErrorFragment(event, bundle);
    }

    /** Returns either a new Honeycomb+ or a new support library DialogFragment. */
    @NonNull
    protected abstract T createErrorFragment(ThrowableFailureEvent event, Bundle arguments);

    /** May be overridden to provide custom error title. */
    @NonNull
    protected String getTitleFor(ThrowableFailureEvent event, Bundle arguments) {
        return config.resources.getString(config.defaultTitleId);
    }

    /** May be overridden to provide custom error messages. */
    @NonNull
    protected String getMessageFor(@NonNull ThrowableFailureEvent event, Bundle arguments) {
        int msgResId = config.getMessageIdForThrowable(event.throwable);
        return config.resources.getString(msgResId);
    }

    public static class Support extends ErrorDialogFragmentFactory<Fragment> {

        public Support(ErrorDialogConfig config) {
            super(config);
        }

        @NonNull
        protected Fragment createErrorFragment(ThrowableFailureEvent event, Bundle arguments) {
            ErrorDialogFragments.Support errorFragment = new ErrorDialogFragments.Support();
            errorFragment.setArguments(arguments);
            return errorFragment;
        }

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class Honeycomb extends ErrorDialogFragmentFactory<android.app.Fragment> {

        public Honeycomb(ErrorDialogConfig config) {
            super(config);
        }

        @NonNull
        protected android.app.Fragment createErrorFragment(ThrowableFailureEvent event, Bundle arguments) {
            ErrorDialogFragments.Honeycomb errorFragment = new ErrorDialogFragments.Honeycomb();
            errorFragment.setArguments(arguments);
            return errorFragment;
        }

    }
}