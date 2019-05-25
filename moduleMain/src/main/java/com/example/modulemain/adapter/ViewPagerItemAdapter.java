package com.example.modulemain.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.modulebase.http.HttpManager;
import com.example.modulebase.http.HttpResponseSubscriber;
import com.example.modulebase.http.RpcHelper;
import com.example.modulecommon.BuildConfig;
import com.example.modulecommon.bean.modulemain.AppBean;
import com.example.modulecommon.common.ARouteContants;
import com.example.modulecommon.exception.HttpThrowable;
import com.example.modulemain.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ViewPagerItemAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private PackageManager mPackageManager;

    private List<AppBean> mInfoList = new ArrayList<>();

    public ViewPagerItemAdapter(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        mPackageManager = context.getPackageManager();
    }

    public void setData(List<AppBean> infos) {
        mInfoList = infos;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return mInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = mInflater.inflate(R.layout.item_app, null);
            convertView.setLayoutParams(new GridView.LayoutParams(mContext.getResources()
                    .getDisplayMetrics().widthPixels / 3,
                    mContext.getResources().getDisplayMetrics().widthPixels / 3));
            holder.relativeLayout = convertView.findViewById(R.id.item_layout);
            holder.appIcon = convertView.findViewById(R.id.app_icon);
            holder.appName = convertView.findViewById(R.id.app_name);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        final AppBean appBean = mInfoList.get(position);
        holder.appName.setText(appBean.getTitle());
        if (!TextUtils.isEmpty(appBean.getIcon())) {
            //通过URL加载图片
        } else {
            //设置默认图片
        }
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (appBean.getPackageName().equals("com.example.modulejscalljava")) {//路由跳转
                    /**
                     * 发起路由跳转
                     * 判断是组件开发模式还是集成开发模式
                     * 组件开发模式路由无法跳转到其它模块(因为每一个组件在该模式下相当于一个apk)
                     */
                    if (BuildConfig.ISDEBUG.equals("true")) {
                        ARouter.getInstance().build(ARouteContants.ModuleMain.MAIN_ACTIVITY).navigation();
                    } else {
                        ARouter.getInstance().build(ARouteContants.ModuleJSCallJava.MAIN_ACTIVITY).navigation();
                    }
                } else if (appBean.getPackageName().equals("com.tencent.mobileqq")) {//包名跳转
                    mContext.startActivity(mContext.getPackageManager().getLaunchIntentForPackage("com.tencent.mobileqq"));
                } else if(appBean.getPackageName().equals("Dialog")){
                    ARouter.getInstance().build(ARouteContants.ModuleMain.DIALOG_ACTIVITY).navigation();
                } else {//action跳转
                    HttpManager.getInstance().getHttpService().testHttp("api/data/Android/10/1")
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new HttpResponseSubscriber<String>() {

                                @Override
                                public void onSuccess(String result) {
                                    Log.i("TAG", result);
                                }

                                @Override
                                public void _onError(HttpThrowable e) {

                                }
                            });
                }
            }
        });
        return convertView;
    }

    class Holder {
        RelativeLayout relativeLayout;
        ImageView appIcon;
        TextView appName;
    }
}
