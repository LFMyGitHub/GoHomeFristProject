package com.example.modulecommon.common;

public class ARouteContants {
    public static class ModuleMain {
        public static final String MAIN = "/main/";

        public static final String MAIN_ACTIVITY = MAIN + "moduleMain";
        public static final String DIALOG_ACTIVITY = MAIN + "moduleDialog";
        public static final String CAMERA_ACTIVITY = MAIN + "moduleCamera";
        public static final String MY_CAMERA_ACTIVITY = MAIN + "moduleMyCamera";
        public static final String JSON_ACTIVITY = MAIN + "moduleJSON";
    }

    public static class ModuleJSCallJava {
        public static final String JS_CALL_JAVA = "/jsCallJava/";

        public static final String MAIN_ACTIVITY = JS_CALL_JAVA + "moduleJSCallJava";
    }
}
