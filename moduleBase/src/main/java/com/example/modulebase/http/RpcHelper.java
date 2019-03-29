package com.example.modulebase.http;

import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.example.modulecommon.utils.EncryptUtil;
import com.example.modulecommon.utils.GsonUtil;

import java.util.Map;

/**
 * 调用接口助手类
 */
public class RpcHelper {
    public static final String OPERATION_TYPE = "operationType"; // http请求中参数key
    public static final String REQUEST_DATA = "requestData"; // http请求中参数key
    public static final String D = "d"; // http请求中参数key

    /**
     * 生成http请求参数map
     *
     * @param method      api接口方法全路径
     * @param requestBean 请求对象
     * @return 请求参数map
     */
    public static Map<String, String> getParamMap(
            String method,
            Object requestBean
    ) {
        String requestData = GsonUtil.DateBean2Json(requestBean);
        Log.i("TAG", "requestData:" + requestData);
        Map<String, String> map = new ArrayMap<>();
        map.put(OPERATION_TYPE, method);
        map.put(REQUEST_DATA, requestData);
        map.put(D, EncryptUtil.sha512(requestData));
        return map;
    }

}
