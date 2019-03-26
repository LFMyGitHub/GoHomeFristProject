package com.example.modulemain.adapter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.modulecommon.bean.modulemain.AppBean;
import com.example.modulemain.R;

import java.util.ArrayList;
import java.util.List;

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
    public View getView(int position, View convertView, ViewGroup parent) {
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
        if(!TextUtils.isEmpty(appBean.getIcon())){
            //通过URL加载图片
        }else {
            //设置默认图片
        }
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, appBean.getTitle(), Toast.LENGTH_SHORT).show();
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
