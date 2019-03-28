package com.example.modulecommon.utils;

import android.content.Context;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

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
    }
}
