package com.example.modulecommon.common;

import com.example.modulecommon.R;

/**
 * 静态常量
 */
public class Contants {

    public static class AppIcon {
        public static final int[] icons = new int[]{R.mipmap.jxt_app_unicom_coupons_icon, R.mipmap.jxt_app_unicom_card_icon,
                R.mipmap.jxt_app_hftwm_icon, R.mipmap.jxt_app_scan_pay_icon, R.mipmap.jxt_app_bank_transfer_icon,
                R.mipmap.jxt_app_public_payment_icon, R.mipmap.jxt_app_bank_pay_icon, R.mipmap.jxt_app_balance_icon,
                R.mipmap.jxt_app_settings_icon};
        public static final int[] banners = new int[]{R.mipmap.banner01, R.mipmap.banner02, R.mipmap.banner03,
                R.mipmap.banner04, R.mipmap.banner05};
    }

    public static class ModuleMain {
        public static final int GRIDE_VIEW_COLUMN = 3;
        public static final int GRIDE_VIEW_NUMBER = 9;
    }


    public static class ModuleJSCallJava {
        //assets下的文件的test.html所在的绝对路径
        public static final String DEFAULT_URL = "file:///android_asset/test.html";
        //public static final String DEFAULT_URL = "https://61.240.234.60/js/test.html";
    }
}
