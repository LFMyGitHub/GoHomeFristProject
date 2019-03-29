package com.example.modulecommon.utils;

import com.example.modulecommon.BuildConfig;
import com.orhanobut.logger.Logger;

public class LogUtil {
    public static void e(Throwable throwable, String message, Object... args) {
        if (Boolean.getBoolean(BuildConfig.ISDEBUG)) {
            Logger.e(throwable, message, args);
        }
    }
}
