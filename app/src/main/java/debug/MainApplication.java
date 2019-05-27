package debug;

import android.util.Log;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.modulecommon.base.BaseApplication;

public class MainApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("TAG","application oncreat()");
        //打印日志
        ARouter.openLog();
        //开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        ARouter.openDebug();
        //初始化
        ARouter.init(this);
    }
}
