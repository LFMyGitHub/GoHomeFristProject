package com.example.modulebase.arouter;

import android.content.Context;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.modulecommon.BuildConfig;
import com.example.modulecommon.common.ARouteContants;

public class JSToAndroid {
    private Context mContext;
    private Bundle mBundle;

    public JSToAndroid(Context context) {
        this.mContext = context;
    }

    public JSToAndroid(Context context, Bundle bundle) {
        this.mContext = context;
        this.mBundle = bundle;
    }

    /**
     * @param txnAmt
     * @param bussCode
     * @param billQueryInfo
     * @param timestamp
     * @param signature
     */
    @JavascriptInterface
    public void getMessage(String txnAmt, String bussCode, String billQueryInfo,
                           String timestamp, String signature) {
        Toast.makeText(mContext, txnAmt + "=======" + bussCode, Toast.LENGTH_LONG).show();
        /**
         * 发起路由跳转
         * 判断是组件开发模式还是集成开发模式
         * 组件开发模式路由无法跳转到其它模块(因为每一个组件在该模式下相当于一个apk)
         */
        if (BuildConfig.ISDEBUG.equals("true")) {
            ARouter.getInstance().build(ARouteContants.ModuleJSCallJava.MAIN_ACTIVITY).navigation();
        }else {
            ARouter.getInstance().build(ARouteContants.ModuleMain.MAIN_ACTIVITY).navigation();
        }
    }
}
