package com.example.modulecommon.utils;

import com.example.modulecommon.bean.modulebase.MyDateSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;
import java.util.Date;

public class GsonUtil {
    private static Gson gson = null;

    static {
        if (gson == null) {
            gson = new Gson();
        }
    }

    private GsonUtil() {}

    /**
     * 序列化包含Date的对象
     *
     * @param bean
     * @return
     */
    public static String DateBean2Json(Object bean) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new MyDateSerializer())
                .setDateFormat(DateFormat.LONG).disableHtmlEscaping().create();
        return gson.toJson(bean);
    }
}
